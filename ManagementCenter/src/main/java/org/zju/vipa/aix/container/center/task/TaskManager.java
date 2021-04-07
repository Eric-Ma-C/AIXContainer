package org.zju.vipa.aix.container.center.task;

import org.zju.vipa.aix.container.api.entity.TaskBriefInfo;
import org.zju.vipa.aix.container.center.ManagementCenter;
import org.zju.vipa.aix.container.center.db.AixDbManager;
import org.zju.vipa.aix.container.center.db.AtlasDbManager;
import org.zju.vipa.aix.container.center.log.LogUtils;
import org.zju.vipa.aix.container.center.network.ServerMessage;
import org.zju.vipa.aix.container.common.config.AIXEnvConfig;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.db.entity.aix.FinishedTask;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;
import org.zju.vipa.aix.container.common.env.EnvError;
import org.zju.vipa.aix.container.common.env.ErrorParser;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
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
    private Map<String, ConcurrentLinkedDeque<Message>> serialTaskMessageMap;

    /**
     * 按token存储的消息队列,存放随心跳信息发送的消息,一般为轻量级控制操作,
     * 不应该影响到正在执行的任务,比如开启详细实时日志上传，< token,messages >
     */
    private Map<String, ConcurrentLinkedDeque<Message>> heartbeatMessageMap;
    /**
     * client-任务  映射表，< token,task >
     */
    private Map<String, Task> taskMap;
    /**
     * 记录每个容器最近一次指令执行成功状态，< token , Boolean >
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

        serialTaskMessageMap = new ConcurrentHashMap<>();
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


    /**
     * 删除无心跳client的数据
     */
    public void removeDeadClientByToken(String token) {
        serialTaskMessageMap.remove(token);
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
        return serialTaskMessageMap.get(token);
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
     * 包括存储在task的errorQueue中的指令
     *
     * @param token
     * @return: Message 为空则表示当前任务已执行完毕,准备抢新的任务
     */
    public Message getExistingTaskCmds(String token) {
//        addErrorQueueMsgs2SerialMQ(token,);
        Message message = getMessageByToken(token);
        if (message != null) {
            /** 消息队列中有待发送消息，直接取出 */
            return message;
        }


        /** 若没有待发送消息，先判断上次client指令执行是否成功*/
        Boolean shellResult = shellResultMap.get(token);
        LogUtils.debug("token={},shellResult={}", token, shellResult);
        if (shellResult != null && shellResult.booleanValue()) {
            /** 若成功则说明当前任务成功完成,可以移除任务,再抢新的任务 */
            //任务执行成功,删除任务
            Task task = taskMap.remove(token);
            shellResultMap.remove(token);
            ManagementCenter.getInstance().updateTaskBriefInfo(token, null);

            AtlasDbManager.getInstance().setTaskFinished(task.getId());
            AixDbManager.getInstance().insertFinishedTask(
                new FinishedTask(ManagementCenter.getInstance().getClientIdByToken(token),
                    task.getId(),"SUCCESS",task.getStartTime(),new Date(),
                    ManagementCenter.getInstance().getTaskLogsByToken(token)));

            LogUtils.info("{}任务执行成功!", token);
        } else {
            /** 若失败则说明当前任务完成,返回值非零,也要移除任务,再抢新的任务 */
            handleTaskFailed(token,"FAILED");
        }
        return new ServerMessage(Intent.YOU_CAN_GRAB_TASK);
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

//        Message message = getExistingTaskCmds(token);
//        if (message != null) {
//            return message;
//        }


        /** 若没有待发送消息 且 上次client指令执行没有成功,则需要尝试添加修复环境的命令;
         *
         * 若没有上次执行状态则抢新的任务 */
        synchronized (token.intern()) {
            /** 首先判断有无正在执行的任务 */
            Task task = taskMap.get(token);
            if (task == null) {
                /** 没有正在执行的任务，尝试抢新的任务 */
                String id = ManagementCenter.getInstance().getClientIdByToken(token);
                if (id==null){
                    LogUtils.error("askForCmds() 容器token:{}不存在",token);
                    return new ServerMessage(Intent.GRAB_TASK_FAILED);
                }
                task = AtlasDbManager.getInstance().grabTask(id);
                if (task == null) {
                    /** 没有抢到任务 */
                    LogUtils.info("{}:暂未抢到任务，请耐心等待...", token);
                    return new ServerMessage(Intent.GRAB_TASK_FAILED);
                }
                LogUtils.info("{}:抢到任务{}", token, task);

                task.setStartTime(new Date());
                /** 抢到的任务放到map中 */
                taskMap.put(token, task);
                ManagementCenter.getInstance().updateTaskBriefInfo(token, new TaskBriefInfo(task));

                String codePath = task.getCodePath();
                String modelArgs = task.getModelArgs();
                /** 更新conda源 */
                String updataCondaSrcCmds = AIXEnvConfig.UPDATE_CONDA_SOURCE_CMD;




//

                /** test 添加待发送任务至列表 */
                /** 1.删除虚拟环境配置 */
                ServerMessage removeEnvMsg = new ServerMessage(Intent.SHELL_TASK, AIXEnvConfig.CONDA_REMOVE_ALL_CMD);
//                addSerialMessage2Tail(token, removeEnvMsg);


//                addSerialMessage2Tail(token, new ServerMessage(Intent.SHELL_TASK, "sudo apt-get clean && sudo mv /var/lib/apt/lists /var/lib/apt/lists.old && sudo mkdir -p /var/lib/apt/lists/partial && sudo apt-get clean"));
//                addSerialMessage(token, new ServerMessage(Intent.SHELL_TASK, AIXEnvConfig.getChangeAptSourceCmd()));
//                addSerialMessage(token, new ServerMessage(Intent.SHELL_TASK, "sudo apt-get update"));


//                addSerialMessage(token, new ServerMessage(Intent.SHELL_TASK, updataCondaSrcCmds));
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, "source /home/aix/.bashrc && echo $PATH"));
                /** test 检查shell */
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, "echo $PATH"));
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, "echo $0"));
//                addMessage(token, new ServerMessage(Intent.SHELL_TASK, "echo $-"));

                /** 如果不通过下载,一般将模型挂载进容器,在数据库写入路径 */
                if (DebugConfig.IS_DOWNLOAD_MODULE) {
                    /** 告诉容器下载model */
                    addSerialMessage2Tail(token, new ServerMessage(Intent.DOWNLOAD_MODEL, task.getModelId()));
                    /** 告诉容器下载dataset */
                    addSerialMessage2Tail(token, new ServerMessage(Intent.DOWNLOAD_DATASET, task.getDatasetId()));
                }

                /** 2.conda环境配置指令                  分成两条指令执行，否则可能会卡住? */
                String condaEnvCreateCmds = AIXEnvConfig.getCondaEnvCreateCmds(task);
                Message condaMsg = new ServerMessage(Intent.SHELL_TASK, condaEnvCreateCmds);
//                addSerialMessage2Tail(token, condaMsg);


                /** 3.preCmds       配置环境结束后，任务启动前，附加执行的代码,可以用来调整环境等 */
                String preCmds = task.getPreCmds();
                if (preCmds != null && !"".equals(preCmds)) {
//                    addSerialMessage2Tail(token, new ServerMessage(Intent.SHELL_TASK, preCmds));
                }

                /** 4.任务启动指令 */
                String startCmds = AIXEnvConfig.getStartCmds(task, token);
                Message startMsg = new ServerMessage(Intent.SHELL_TASK, startCmds);
                startMsg.addCustomData("codePath", codePath);
                startMsg.addCustomData("modelArgs", modelArgs);
                addSerialMessage2Tail(token, startMsg);


                return getMessageByToken(token);


            } else {
                /**  task ！= null，说明有任务正在配置环境 */


                LogUtils.info("{}发现已有任务{}", token, task);

                if (task.isFailed()) {
                    /** 未知原因启动失败次数过多，导致任务执行失败 */
                    handleTaskFailed(token,"ENV_FAILED");
                    return null;
                }

                /** 没有待执行指令，查看是否有待修复的运行错误 */
                ConcurrentLinkedQueue<EnvError> errorQueue = task.getErrorQueue();
                if (errorQueue.isEmpty()) {
//                    /** 没有检测到可解决错误，直接重启，报错可能会改变，再次尝试修复 */
//                    LogUtils.error("{}:\n Client训练遇到了未知问题，正在尝试重新启动模型训练...", token);
//
//                    String codePath = task.getCodePath();
//                    String modelArgs = task.getModelArgs();
//                    Message msg = new ServerMessage(Intent.SHELL_TASK, AIXEnvConfig.getStartCmds(task, token));
//                    msg.addCustomData("codePath", codePath);
//                    msg.addCustomData("modelArgs", modelArgs);
//                    addSerialMessage2Tail(token, msg);

                } else {
                    /** 检测到可解决错误，全部添加到SerialMQ */
                    addErrorQueueMsgs2SerialMQ(token, errorQueue);
                }


                return getExistingTaskCmds(token);

            }
        }

//        /** 待发送的指令消息 */
//        Message toSendMsg = getMessageByToken(token);
//
//        return toSendMsg;
    }


    private void addErrorQueueMsgs2SerialMQ(String token, ConcurrentLinkedQueue<EnvError> errorQueue) {
        /** 检测到可解决错误,一次加入待发送消息队列 */
        while (!errorQueue.isEmpty()) {
            EnvError error = errorQueue.poll();
            List<String> repairCmds = error.getRepairCmdList();
            for (int i = repairCmds.size() - 1; i >= 0; i--) {
                /** 添加一条待发送任务至列表 */
                addSerialMessage2Head(token, new ServerMessage(Intent.SHELL_TASK, repairCmds.get(i)));
            }
        }
    }



    /**
     * 任务执行失败的处理
     */
    private void handleTaskFailed(String token,String taskStatus) {

        Task task = taskMap.remove(token);
        shellResultMap.remove(token);
        /** 删除task消息队列(如果有的话) */
        serialTaskMessageMap.remove(token);

        /** 更新容器显示状态 */
        ManagementCenter.getInstance().updateRunningCmds(token, "");
        ManagementCenter.getInstance().updateTaskBriefInfo(token, null);

        /** 记录任务执行情况至数据库 */
        FinishedTask finishedTask = new FinishedTask(ManagementCenter.getInstance().getClientIdByToken(token),
            task.getId(), taskStatus, task.getStartTime(), new Date(),
            ManagementCenter.getInstance().getTaskLogsByToken(token));
        LogUtils.info("insert FinishedTask:{}",finishedTask);

        AixDbManager.getInstance().insertFinishedTask(finishedTask);


        //修改数据库
        AtlasDbManager.getInstance().setTaskFailed(task.getId());

        LogUtils.error("{}任务执行失败，重新开始抢任务!", token);

    }


    /**
     * 从队列中获取
     *
     * @return 若token对应容器没有待发送消息，返回null
     */
    private Message getMessageByToken(String token) {
        Message message = null;

        synchronized (token.intern()) {
            ConcurrentLinkedDeque<Message> messageList = serialTaskMessageMap.get(token);
            if (messageList != null) {
                LogUtils.debug("{}队列还剩{}个待发送消息", token, messageList.size());
                message = messageList.poll();
                if (messageList.size() == 0) {
                    serialTaskMessageMap.remove(token);
                }
            }
        }
        if (message == null) {
            return null;
        }

        LogUtils.info("{}:\n从队列中获取待发送消息：{}", message.getTokenSuffix(), message);

        if (message != null && Intent.SHELL_TASK.match(message.getIntent())) {
            ManagementCenter.getInstance().updateRunningCmds(token, message.getValue());
        } else {
            ManagementCenter.getInstance().updateRunningCmds(token, "");
        }
        return message;
    }

    /**
     * 新增消息到队尾
     */
    private void addSerialMessage2Tail(String token, Message msg) {
        /** 锁粒度细化为token */
        synchronized (token.intern()) {
            ConcurrentLinkedDeque<Message> messageList = serialTaskMessageMap.get(token);

            if (messageList == null) {
                messageList = new ConcurrentLinkedDeque<>();
                serialTaskMessageMap.put(token, messageList);
            }
            messageList.offer(msg);
        }
        LogUtils.info("{}:\n添加待发送serial消息{}", msg.getTokenSuffix(), msg);
    }

    /**
     * 新增消息到队首
     */
    private void addSerialMessage2Head(String token, Message msg) {
        /** 锁粒度细化为token */
        synchronized (token.intern()) {
            ConcurrentLinkedDeque<Message> messageList = serialTaskMessageMap.get(token);

            if (messageList == null) {
                messageList = new ConcurrentLinkedDeque<>();
                serialTaskMessageMap.put(token, messageList);
            }
            messageList.addFirst(msg);
        }
        LogUtils.info("{}:\n添加待发送serial消息{}至队首", msg.getTokenSuffix(), msg);
    }

    /**
     * 返回待发送的heartbeat信息
     */
    public Message getHeartbeatMessage(String token) {
        LogUtils.debug("getHeartbeatMessage() token={} ", token);
        ConcurrentLinkedDeque<Message> messageList = heartbeatMessageMap.get(token);
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
    protected void addHeartbeatMessage(String token, Message msg) {
        /** 锁粒度细化为token */
//        synchronized (token.intern()) {
        ConcurrentLinkedDeque<Message> messageList = heartbeatMessageMap.get(token);

        if (messageList == null) {
            messageList = new ConcurrentLinkedDeque<>();
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
        String errorInfo = message.getValue();
        String token = message.getToken();


        synchronized (token.intern()) {
            Task task = taskMap.get(token);
            String runningCmds = ManagementCenter.getInstance().getRunningCmdsByToken(token);


            /** 目前容器仅支持单任务，可以直接添加至容器Task对象的error列表中 */
            if (task != null) {
                EnvError error = ErrorParser.handle(token, runningCmds, errorInfo, task);

//                if (task.getPreCmds().equals(runningCmds)) {
//                    /** 是preCmds，直接重新运行preCmds */
//                    error = new EnvError(ErrorType.PRE_CMDS_ERROR, errorInfo, task.getPreCmds());
//                }


                if (error != null) {
//                    task.clearUnknownErrorTime();
                    task.addEnvError(error);
                } else {
                    //未知错误达到一定程度就会判为任务执行失败
                    task.addUnknownErrorTime();
                    LogUtils.debug("Task {} UnknownErrorTime={}", task.getId(), task.getUnknownErrorTime());
                }
            }
        }
    }

    /**
     * 强行停止容器任务
     * 删除任务信息
     *
     * @param token
     * @return:
     */
    protected void userStopTask(String token) {
        LogUtils.worning("User Stop Task");
        handleTaskFailed(token,"CANCEL");
    }
}
