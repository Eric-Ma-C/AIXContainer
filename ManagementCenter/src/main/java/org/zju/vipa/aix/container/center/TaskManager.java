package org.zju.vipa.aix.container.center;

import org.zju.vipa.aix.container.center.db.DbManager;
import org.zju.vipa.aix.container.center.db.entity.Task;
import org.zju.vipa.aix.container.center.env.EnvError;
import org.zju.vipa.aix.container.center.env.ErrorParser;
import org.zju.vipa.aix.container.center.network.ServerMessage;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.config.AIXEnvConfig;
import org.zju.vipa.aix.container.config.DebugConfig;
import org.zju.vipa.aix.container.exception.AIXBaseException;
import org.zju.vipa.aix.container.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.message.Intent;
import org.zju.vipa.aix.container.message.Message;

import java.util.Map;
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

        if (TaskManagerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }

        messageMap = new ConcurrentHashMap<>();
        taskMap = new ConcurrentHashMap<>();
//        /** 保证hashset的同步性 */
//        tokenSet = Collections.synchronizedSet(new HashSet<String>());


        //test code,to be deleted
//        Message msg = new Message(Intent.SHELL_TASK, "source /home/aix/miniconda3/bin/activate clean_yolo && python  /nfs2/sontal/codes/TrainerProxy/main.py");
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

        Message message = getMessageByToken(token);
        if (message != null) {
            /** 消息队列中有待发送消息，直接取出 */
            return message;
        }

        /** 若没有待发送消息，则尝试添加消息，首先判断有无正在执行的任务 */
        synchronized (token.intern()) {
            Task task = taskMap.get(token);
            if (task == null) {
                /** 没有正在执行的任务，尝试抢新的任务 */
                String id = ManagementCenter.getInstance().getIdByToken(token);
                task = DbManager.getInstance().grabTask(id);
                if (task == null) {
                    /** 没有抢到任务 */
                    LogUtils.info("{}:\n暂未抢到任务，请耐心等待...", token.substring(token.length() - 9));
                    return null;
                }
                LogUtils.info("{}:\n抢到任务{}", token.substring(token.length() - 9), task);

                /** 抢到的任务放到map中 */
                taskMap.put(token, task);
                String codePath = task.getCodePath();
                String modelArgs = task.getModelArgs();
                String updataCondaSrcCmds = AIXEnvConfig.UPDATE_CONDA_SOURCE_CMD;
//         todo 测试 跳过配conda环境
                String cmds = AIXEnvConfig.getCondaEnvCreateCmds(codePath) + " && " + AIXEnvConfig.getStartCmds(codePath, modelArgs);
//                String cmds =  AIXEnvConfig.getStartCmds(codePath,modelArgs);
//

                /** 添加待发送任务至列表 */
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, updataCondaSrcCmds));
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, "source /home/aix/.bashrc && echo $PATH"));
                /** 检查shell */
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, "echo $PATH"));
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, "echo $0"));
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, "echo $-"));

                //如果不通过下载,一般将模型挂载进容器,在数据库写入路径
                if (DebugConfig.IS_DOWNLOAD_MODULE) {
                    /** 告诉容器下载model */
                    addMessage(token, new ServerMessage(Intent.DOWNLOAD_MODEL, task.getModelId()));
                    /** 告诉容器下载dataset */
                    addMessage(token, new ServerMessage(Intent.DOWNLOAD_DATASET, task.getDatasetId()));
                }

                Message msg = new ServerMessage(Intent.SHELL_TASK, cmds);
                msg.addCustomData("codePath", codePath);
                msg.addCustomData("modelArgs", modelArgs);
                addMessage(token, msg);


            } else {
                /**  task ！= null，说明有任务正在配置环境 */
                LogUtils.info("{}发现已有任务{}", token.substring(token.length() - 9), task);

                /** 没有待执行任务，查看是否有待修复的运行错误 */
                ConcurrentLinkedQueue<EnvError> errorQueue = task.getErrorQueue();
                if (errorQueue.isEmpty()) {
                    /** 没有检测到可解决错误，直接重启，报错可能会改变，再次尝试修复 */
                    LogUtils.error("{}:\n Client遇到了一些问题，正在尝试重新启动模型训练...", token.substring(token.length() - 9));

                    String codePath = task.getCodePath();
                    String modelArgs = task.getModelArgs();
                    Message msg = new ServerMessage(Intent.SHELL_TASK, AIXEnvConfig.getStartCmds(codePath, modelArgs));
                    msg.addCustomData("codePath", codePath);
                    msg.addCustomData("modelArgs", modelArgs);
                    addMessage(token, msg);


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
     *
     * @return 若token对应容器没有待发送消息，返回null
     */
    private Message getMessageByToken(String token) {
        Message message = null;

        synchronized (token.intern()) {
            ConcurrentLinkedQueue<Message> messageList = messageMap.get(token);
            if (messageList != null) {
                message = messageList.poll();
            }
        }
        if (message == null) {
            return null;
        }

        LogUtils.info("{}:\n从队列中获取待发送消息：{}", token.substring(token.length() - 9), message);
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
        LogUtils.info("{}:\n添加待发送消息{}", token.substring(token.length() - 9), msg);
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
            EnvError error = ErrorParser.handle(token, value, task);

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
