package org.zju.vipa.aix.container.center.task;

import org.zju.vipa.aix.container.api.entity.TaskBriefInfo;
import org.zju.vipa.aix.container.center.ManagementCenter;
import org.zju.vipa.aix.container.center.db.DbManager;
import org.zju.vipa.aix.container.center.network.ServerMessage;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.common.config.AIXEnvConfig;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.entity.Task;
import org.zju.vipa.aix.container.common.env.EnvError;
import org.zju.vipa.aix.container.common.env.ErrorParser;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;

import java.util.Map;
import java.util.Queue;
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
     * 按token存储的消息队列,存放需要顺序执行的消息，< token,messages >
     */
    private Map<String, ConcurrentLinkedQueue<Message>> serialMessageMap;

    /**
     * 按token存储的消息队列,存放随心跳信息发送的消息,一般为轻量级操作,不应该影响到正在执行的任务,比如开启详细实时日志上传，< token,messages >
     */
    private Map<String, ConcurrentLinkedQueue<Message>> heartbeatMessageMap;
    /**
     * client-任务映射表，< token,task >
     */
    private Map<String, Task> taskMap;
    /**
     * 最近一次指令执行状态映射，< token,Boolean >
     */
    private Map<String, Boolean> shellResultMap;

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

        serialMessageMap = new ConcurrentHashMap<>();
        heartbeatMessageMap = new ConcurrentHashMap<>();
        taskMap = new ConcurrentHashMap<>();
        shellResultMap = new ConcurrentHashMap<>();
//        /** 保证hashset的同步性 */
//        tokenSet = Collections.synchronizedSet(new HashSet<String>());


        //test code,to be deleted
//        Message msg = new Message(Intent.SHELL_TASK, "source /home/aix/miniconda3/bin/activate clean_yolo && python  /nfs2/sontal/codes/TrainerProxy/main.py");
//        addMessage(NetworkConfig.TEST_CONTAINER_TOKEN, msg);
    }

    public static TaskManager getInstance() {
        return TaskManagerHolder.INSTANCE;
    }


    /** 删除无心跳client的数据 */
    public void removeDeadClient(String token) {
        serialMessageMap.remove(token);
        heartbeatMessageMap.remove(token);
        taskMap.remove(token);
        shellResultMap.remove(token);

    }

    /**
     * 正在与平台通信的训练容器数量
     * 也就是有任务的client数量
     */
    protected int getActiveClientNum() {
        return taskMap.size();
    }


    /**
     * 获取某容器的待发送队列
     */
    protected Queue<Message> getMessageQueueByToken(String token) {
        return serialMessageMap.get(token);
    }

    /**
     * 获取某容器的当前任务
     */
    protected Task getTaskByToken(String token) {
        return taskMap.get(token);
    }

    /**
     * 设置最后一次指令执行结果,辅助判断任务是否完成
     */
    public void setLastShellResult(String token, boolean result) {

        shellResultMap.put(token, result);

    }


    /**
     * 获取当前task已有的未发送的指令
     *
     * @param token
     * @return: Message 为空则表示当前任务已执行完毕,准备抢新的任务
     */
    public Message getExistingTaskCmds(String token) {
        Message message = getMessageByToken(token);
        if (message != null) {
            /** 消息队列中有待发送消息，直接取出 */
            return message;
        }


        /** 若没有待发送消息，先判断上次client指令执行是否成功,若成功则说明当前任务完成,可以移除任务,再抢新的任务 */
        Boolean shellResult = shellResultMap.get(token);
        LogUtils.debug("token={},shellResult={}", token, shellResult);
        if (shellResult != null && shellResult.booleanValue()) {
            //任务执行成功,删除任务
            Task task = taskMap.remove(token);
            shellResultMap.remove(token);
            ManagementCenter.getInstance().updateTaskBriefInfo(token, new TaskBriefInfo());

            DbManager.getInstance().setTaskFinished(task.getId());
            LogUtils.info("{}任务执行结束!", token);
            return new ServerMessage(Intent.YOU_CAN_GRAB_TASK);
        } else {
            return null;
        }
    }

    /**
     * 在空闲时尝试获取新的任务
     * 当容器抢到一个任务的时候，对应token下若有待发送消息，则依次返回队列中的消息
     * 当容器没有已接受任务时，则尝试请求一个任务
     *
     * @param token
     * @return: zju.vipa.aix.container.message.Message
     */
    public Message askForCmds(String token) {

        Message message = getExistingTaskCmds(token);
        if (message != null) {
            return message;
        }


        /** 若没有待发送消息且
         * 若上次client指令执行没有成功,则需要尝试添加修复环境的命令;
         * 若没有上次执行状态则抢新的任务 */
        synchronized (token.intern()) {
            /** 首先判断有无正在执行的任务 */
            Task task = taskMap.get(token);
            if (task == null) {
                /** 没有正在执行的任务，尝试抢新的任务 */
                String id = ManagementCenter.getInstance().getIdByToken(token);
                task = DbManager.getInstance().grabTask(id);
                if (task == null) {
                    /** 没有抢到任务 */
                    LogUtils.info("{}:\n暂未抢到任务，请耐心等待...", token);
                    return new ServerMessage(Intent.GRAB_TASK_FAILED);
                }
                LogUtils.info("{}:\n抢到任务{}", token, task);

                /** 抢到的任务放到map中 */
                taskMap.put(token, task);
                ManagementCenter.getInstance().updateTaskBriefInfo(token, new TaskBriefInfo(task));

                String codePath = task.getCodePath();
                String modelArgs = task.getModelArgs();
                String updataCondaSrcCmds = AIXEnvConfig.UPDATE_CONDA_SOURCE_CMD;
//         todo 测试 跳过配conda环境
                /** 分成两条指令执行，否则可能会卡住? */
//                String condaEnvCreateCmds = AIXEnvConfig.getCondaEnvCreateCmds(codePath);
                String condaEnvCreateCmds = "ls";
                String startCmds = AIXEnvConfig.getStartCmds(codePath, modelArgs);
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
                    addSerialMessage(token, new ServerMessage(Intent.DOWNLOAD_MODEL, task.getModelId()));
                    /** 告诉容器下载dataset */
                    addSerialMessage(token, new ServerMessage(Intent.DOWNLOAD_DATASET, task.getDatasetId()));
                }

                Message msg1 = new ServerMessage(Intent.SHELL_TASK, condaEnvCreateCmds);
                Message msg2 = new ServerMessage(Intent.SHELL_TASK, startCmds);
                msg2.addCustomData("codePath", codePath);
                msg2.addCustomData("modelArgs", modelArgs);
                addSerialMessage(token, msg1);
                addSerialMessage(token, msg2);


            } else { /**  task ！= null，说明有任务正在配置环境 */

                LogUtils.info("{}发现已有任务{}", token, task);

                /** 没有待执行任务，查看是否有待修复的运行错误 */
                ConcurrentLinkedQueue<EnvError> errorQueue = task.getErrorQueue();
                if (errorQueue.isEmpty()) {
                    /** 没有检测到可解决错误，直接重启，报错可能会改变，再次尝试修复 */
                    LogUtils.error("{}:\n Client遇到了一些问题，正在尝试重新启动模型训练...", token);

                    String codePath = task.getCodePath();
                    String modelArgs = task.getModelArgs();
                    Message msg = new ServerMessage(Intent.SHELL_TASK, AIXEnvConfig.getStartCmds(codePath, modelArgs));
                    msg.addCustomData("codePath", codePath);
                    msg.addCustomData("modelArgs", modelArgs);
                    addSerialMessage(token, msg);


                } else {
                    /** 检测到可解决错误,一次加入待发送消息队列 */
                    while (!errorQueue.isEmpty()) {
                        EnvError error = errorQueue.poll();
                        String repairCmds = error.getRepairCmds();
                        /** 添加一条待发送任务至列表 */
                        addSerialMessage(token, new ServerMessage(Intent.SHELL_TASK, repairCmds));
                    }
                }

            }
        }


        /** 待发送的指令消息 */
        Message toSendMsg = getMessageByToken(token);
        if (toSendMsg != null) {
            ManagementCenter.getInstance().updateRunningCmds(token, toSendMsg.getValue());
        } else {
            ManagementCenter.getInstance().updateRunningCmds(token, "");
        }

        return toSendMsg;
    }


    /**
     * 从队列中获取
     *
     * @return 若token对应容器没有待发送消息，返回null
     */
    private Message getMessageByToken(String token) {
        Message message = null;

        synchronized (token.intern()) {
            ConcurrentLinkedQueue<Message> messageList = serialMessageMap.get(token);
            if (messageList != null) {
                message = messageList.poll();
                LogUtils.debug("{}队列还剩{}个待发送消息", token, messageList.size());
                if (messageList.size() == 0) {
                    serialMessageMap.remove(token);
                }
            }
        }
        if (message == null) {
            return null;
        }

        LogUtils.info("{}:\n从队列中获取待发送消息：{}", message.getTokenSuffix(), message);
        return message;
    }

    /**
     * 新增消息
     */
    private void addSerialMessage(String token, Message msg) {
        /** 锁粒度细化为token */
        synchronized (token.intern()) {
            ConcurrentLinkedQueue<Message> messageList = serialMessageMap.get(token);

            if (messageList == null) {
                //TODO 太久没用的token删除,相当于客户端离线
                messageList = new ConcurrentLinkedQueue<>();
                serialMessageMap.put(token, messageList);
            }
            messageList.offer(msg);
//            messageMap.put(token, messageList);
        }
        LogUtils.info("{}:\n添加待发送serial消息{}", msg.getTokenSuffix(), msg);
    }

    /**
     * 返回待发送的heartbeat信息
     */
    public Message getHeartbeatMessage(String token) {
        LogUtils.debug("getHeartbeatMessage() token={} ", token);
        ConcurrentLinkedQueue<Message> messageList = heartbeatMessageMap.get(token);
        if (messageList == null) {
            return null;
        } else {
            Message message = messageList.poll();
            LogUtils.debug("{}心跳信息队列还剩{}个待发送消息", token, messageList.size());

            if (messageList.size() == 0) {
                /** 保证空值messageList == null */
                heartbeatMessageMap.remove(token);
            }
            return message;
        }
    }

    /**
     * 新增随心跳返回的消息
     */
    public void addHeartbeatMessage(String token, Message msg) {
        /** 锁粒度细化为token */
//        synchronized (token.intern()) {
        ConcurrentLinkedQueue<Message> messageList = heartbeatMessageMap.get(token);

        if (messageList == null) {
            messageList = new ConcurrentLinkedQueue<>();
            heartbeatMessageMap.put(token, messageList);
        }
        messageList.offer(msg);
//            messageMap.put(token, messageList);
//        }
        LogUtils.info("{}:\n添加待发送heartbeat消息{}", msg.getTokenSuffix(), msg);
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

}
