package zju.vipa.aix.container.center.env;

import zju.vipa.aix.container.center.ManagementCenter;
import zju.vipa.aix.container.center.db.DbManager;
import zju.vipa.aix.container.center.db.entity.Task;
import zju.vipa.aix.container.center.network.ServerMessage;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.utils.LogUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Date: 2020/3/18 14:55
 * @Author: EricMa
 * @Description: 管理容器任务的执行
 * 给容器派发任务
 */
public class TaskManager {

    /**
     * 消息队列，< token,messages >
     */
    private Map<String, ConcurrentLinkedQueue<Message>> messageMap;
    /**
     * 任务映射，< token,task >
     */
    private Map<String, Task> taskMap;

    /**
     * token锁列表，用于减小并发访问的锁粒度
     */
//    private Set<String> tokenSet;
//    private Queue<SeverMessage> messageQueue;

    private static class TaskManagerHolder {
        private static final TaskManager INSTANCE = new TaskManager();
    }

    private TaskManager() {
        messageMap = new ConcurrentHashMap<>();
        taskMap = new ConcurrentHashMap<>();
//        /** 保证hashset的同步性 */
//        tokenSet = Collections.synchronizedSet(new HashSet<String>());


        //test code,to be deleted
//        Message msg = new Message(Intent.SHELL_TASK, "source /root/miniconda3/bin/activate clean_yolo && python  /nfs2/sontal/codes/TrainerProxy/main.py");
//        addMessage(NetworkConfig.TEST_CONTAINER_TOKEN, msg);
    }

    public static TaskManager getInstance() {
        return TaskManagerHolder.INSTANCE;
    }


    /**
     * 在空闲时尝试获取新的任务
     * 当容器抢到一个任务的时候，对应token下若有待发送消息，则依次返回队列中的消息
     * 当容器没有已接受任务时，则尝试请求一个任务
     *
     * @param token
     * @return: zju.vipa.aix.container.message.Message
     */
    public Message askForWork(String token) {

        synchronized (token.intern()) {
            Task task = taskMap.get(token);
            if (task == null) {
                /** 没有正在执行的任务，尝试抢新的任务 */
                String id = ManagementCenter.getInstance().getIdByToken(token);
                task = DbManager.getInstance().grabTask(id);
                if (task == null) {
                    /** 没有抢到任务 */
                    LogUtils.info(token, "暂未抢到任务，请耐心等待...");
                    return null;
                }
                LogUtils.info(token, "抢到任务" + task);

                /** 抢到的任务放到map中 */
                taskMap.put(token, task);

                String cmds = task.getCondaEnvCreateCmds() + " && " + task.getStartCmds();

//                String cmds = task.getStartCmds();

                /** 添加一条待发送任务至列表 */
                addMessage(token, new ServerMessage(Intent.SHELL_TASK, cmds));


            } else {
                /**  task ！= null，说明有任务正在配置环境 */
                LogUtils.info(token, "发现已有任务" + task);

                ConcurrentLinkedQueue<EnvError> errorQueue = task.getErrorQueue();
                if (errorQueue.isEmpty()) {
                    /** 没有检测到可解决错误，直接重启，报错可能会改变，再次尝试修复 */
                    LogUtils.error("遇到了一些问题，正在尝试重新启动模型训练...");

                    String restartCmds = task.getStartCmds();

                    addMessage(token, new ServerMessage(Intent.SHELL_TASK, restartCmds));


                } else {
                    /** 检测到可解决错误,一次加入待发送消息队列 */
                    while (!errorQueue.isEmpty()) {
                        EnvError error = errorQueue.poll();
                        String repairCmds = error.getRepairCmds();
                        /** 添加一条待发送任务至列表 */
                        addMessage(token, new ServerMessage(Intent.SHELL_TASK, repairCmds));
                    }
                }

            }
        }

        return getMessageByToken(token);
    }


    /**
     * 从队列中获取
     */
    private Message getMessageByToken(String token) {
        Message message = null;

        synchronized (token.intern()) {
            ConcurrentLinkedQueue<Message> messageList = messageMap.get(token);
            if (messageList != null) {
                message = messageList.poll();
            }
        }

        LogUtils.info(token, "从队列中获取待发送消息：" + message);
        return message;
    }

    /**
     * 新增消息
     */
    public void addMessage(String token, Message msg) {
        /** 锁粒度细化为token */
        synchronized (token.intern()) {
            ConcurrentLinkedQueue<Message> messageList = messageMap.get(token);

            if (messageList == null) {
                messageList = new ConcurrentLinkedQueue<>();
                messageMap.put(token, messageList);
            }
            messageList.offer(msg);
//            messageMap.put(token, messageList);
        }
        LogUtils.info(token, "添加待发送消息：" + msg);
    }


    /**
     * 自动检测，处理一些环境配置问题
     * 保存检测到的错误信息，放入对应client的task中暂存
     *
     * @param message
     * @return: void
     */
    public void handleError(Message message) {
        String value = message.getValue();
        String token = message.getToken();

        synchronized (token.intern()) {
            Task task = taskMap.get(token);
            EnvError error = ErrorParser.handle(token,value, task);

            /** 目前容器仅支持单任务，可以直接添加至容器Task对象的error列表中 */
            if (task != null && error != null) {
                task.addEnvError(error);
            }
        }


    }

//    private class SeverMessage{
//        private String clientId;
//        private Message message;
//
//        public SeverMessage(String clientId, Message message) {
//            this.clientId = clientId;
//            this.message = message;
//        }
//
//        public String getClientId() {
//            return clientId;
//        }
//
//        public Message getMessage() {
//            return message;
//        }
//    }
}
