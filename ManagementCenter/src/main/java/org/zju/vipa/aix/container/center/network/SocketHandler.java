package org.zju.vipa.aix.container.center.network;

import io.netty.channel.ChannelHandlerContext;
import org.zju.vipa.aix.container.center.ManagementCenter;
import org.zju.vipa.aix.container.center.db.AtlasDbManager;
import org.zju.vipa.aix.container.center.kafka.ClientRealTimeLogProducer;
import org.zju.vipa.aix.container.center.log.Action;
import org.zju.vipa.aix.container.center.log.ClientLogFileManager;
import org.zju.vipa.aix.container.center.netty.NettyIoImpl;
import org.zju.vipa.aix.container.center.task.TaskManager;
import org.zju.vipa.aix.container.center.task.TaskManagerService;
import org.zju.vipa.aix.container.center.util.JwtUtils;
import org.zju.vipa.aix.container.center.log.LogUtils;
import org.zju.vipa.aix.container.common.config.Sources;
import org.zju.vipa.aix.container.common.message.GpuInfo;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.message.SystemBriefInfo;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date: 2020/1/7 16:10
 * @Author: EricMa
 * @Description: 处理接收到的所有容器请求
 */
public class SocketHandler implements Runnable {


    private ServerIO serverIO;

    public SocketHandler(Socket mSocket) {
        serverIO = new SocketIoImpl(mSocket);
    }

    public SocketHandler(ChannelHandlerContext context) {
        serverIO = new NettyIoImpl(context);
    }

    @Override
    public void run() {

        handle(((SocketIoImpl) serverIO).readRequests());

    }

    /**
     * 处理一次Socket请求
     * (短连接)
     *
     * @throws Exception
     */
    public void handle(Message msg) {
        if (msg == null) {
            LogUtils.worning("Requests body is null!");
            return;
        }

        switch (msg.getIntent()) {
            case ASK_FOR_COMMENDS:
                clientIdleAskForCmds(msg.getToken());
                break;
            case CONDA_SOURCE:
                returnNewCondaSource();
                break;
            case GET_CONDA_ENV_FILE_BY_TASKID:
                getCondaEnvFileByTaskId(msg.getValue());
                break;
            case GET_PIP_ENV_FILE_BY_TASKID:
                getPipEnvFileByTaskId(msg.getValue());
                break;
            case GRAB_TASK:
                handleGrabbingTask(msg);
                break;
            case SHELL_BEGIN:
                shellBegin(msg);
                break;
            case SHELL_INFO:
                shellInfo(msg);
                break;
            case SHELL_RESULT:
                shellResult(msg);
                break;
            case SHELL_ERROR:
                shellError(msg);
                break;
            case SHELL_ERROR_HELP:
                shellErrorHandle(msg);
                break;
            case REAL_TIME_LOG:
                handleRealtimeLog(msg);
                break;


            case REGISTER:
                registerContainer(msg);
                break;
            case PING:
                handleHeartbeatGpuInfo(msg);
                break;
            case EXCEPTION:
                handleException(msg);
                break;

            case REQUEST_UPLOAD:
                isAcceptUpload(msg.getValue());
                break;
            case UPLOAD_DATA:
                serverIO.saveData(msg);
                break;
            default:
                break;
        }
//  没有disconnect();
    }


    /**
     * 是否接受上传
     */
    private void isAcceptUpload(String fileName) {
        String serverSpecifiedFileName = fileName;
        if (fileName == null) {
            //平台指定一个日志文件
//            serverSpecifiedFileName = ClientLogFileManager.getInstence().getFilePathByDate("2020-04-19");
            serverSpecifiedFileName = ClientLogFileManager.getInstence().getFilePathByDate(null);
        }
        serverIO.response(new ServerMessage(Intent.UPLOAD_PERMITTED, serverSpecifiedFileName));
    }

    /**
     * 容器已没有待执行Task，问平台有没有任务
     */
    private void clientIdleAskForCmds(String token) {
        Message toSendMsg = TaskManager.getInstance().askForCmds(token);
        if (toSendMsg == null || Intent.GRAB_TASK_FAILED.equals(toSendMsg.getIntent())) {
            /** 平台针对当前任务已没有指令可以执行,告诉容器开始抢新任务*/
            toSendMsg = new ServerMessage(Intent.YOU_CAN_GRAB_TASK);
        }

        LogUtils.info("{}:clientIdleAskForCmds={}", toSendMsg.getIntent());
        serverIO.response(toSendMsg);
    }

    /**
     * 容器实时日志处理
     */
    private void handleRealtimeLog(Message msg) {
//        sendToKafka(msg);
        LogUtils.debug("Realtime log from {}:{}", msg.getTokenSuffix(), msg.getValue());
    }

    /**
     * 处理gpu信息
     * 保存在map中
     *
     * @param:
     * @return:
     */
    private void handleHeartbeatGpuInfo(Message message) {

        GpuInfo gpuInfo = JsonUtils.parseObject(message.getValue(), GpuInfo.class);
        if (gpuInfo != null) {
            LogUtils.info("Heartbeat from {},GPU info:{}", message.getTokenSuffix(), message.getValue());
            ManagementCenter.getInstance().updateGpuInfo(message.getToken(), gpuInfo);
        }
        //取出可能的待发送信息
        Message res = TaskManager.getInstance().getHeartbeatMessage(message.getToken());
        LogUtils.debug("getHeartbeatMessage() return={}", res);
        if (res != null) {
            serverIO.response(res);
        } else {
            serverIO.response(new ServerMessage(Intent.PONG));
        }

    }

    /**
     * 显示异常信息
     *
     * @param:
     * @return:
     */
    private void handleException(Message message) {
        LogUtils.error("{}:\n{}", message.getTokenSuffix(), message.getValue());
    }


    /**
     * 首次连接平台，验证容器的token
     * <p>
     * token 容器上传的jwt token，没有私钥不能伪造
     *
     * @return: void
     */
    private void registerContainer(Message msg) {
        String token = msg.getToken();
        String clientHostIp = msg.getCustomData(Message.HOST_IP_KEY);
        boolean ok = JwtUtils.verify(token);
        Message resMsg;


        /** 尝试获取id号并缓存下来 */
        String id = ok ? ManagementCenter.getInstance().registerClient(token,clientHostIp) : null;
        if (id != null && id.length() > 0) {

            AtlasDbManager.getInstance().updateDeviceLastLoginById(id);

            LogUtils.info(Action.CLIENT_REGISTER, "Container id={} registered successfully!  token={} host_ip={}", id, token, clientHostIp);
            resMsg = new ServerMessage(Intent.REGISTER, "OK");
            //写返回报文
            serverIO.response(resMsg);
        } else {
            resMsg = new ServerMessage(Intent.REGISTER, "DENIED");
            //写返回报文
            serverIO.response(resMsg);
            LogUtils.error(Action.CLIENT_REGISTER, "Container registered failed! token:{} host_ip={}", token, clientHostIp);
        }

    }


    /**
     * 更新conda源
     *
     * @param:
     * @return:
     */
    private void returnNewCondaSource() {
//        Message msg = new Message(Intent.CONDA_SOURCE,"test");
        Message msg = new ServerMessage(Intent.CONDA_SOURCE, Sources.CONDA_SOURCE_TUNA);

        //写返回报文
        serverIO.response(msg);
    }


    /**
     * 抢任务消息处理
     * 平台有任务则下发任务执行
     * 否则继续抢任务
     *
     * @param msg
     * @return: void
     */
    private void handleGrabbingTask(Message msg) {
        String token = msg.getToken();

        /** 根据token获取id */
        String id = ManagementCenter.getInstance().getIdByToken(token);

        if (id == null) {
            LogUtils.error("Grabbing task no such a device:token={}", msg.getToken());
            return;
        }

        SystemBriefInfo info = JsonUtils.parseObject(msg.getValue(), SystemBriefInfo.class);
        if (info == null) {
            LogUtils.error("Grabbing task info error:{}", msg);
            return;
        }


//        LogUtils.info("{}:\nHeartbeats from client (id={}): IP{} CPU={}%  RAM={}%  time={}",
//            token, id, mSocket.getInetAddress(), info.getCpuRate(), info.getRamRate(),
//            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

        LogUtils.info("{}:\nGrabbing task request from client (id={}): CPU={}%  RAM={}%  time={}",
            token, id, info.getCpuRate(), info.getRamRate(),
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));


        //尝试取出一条待发送消息
        Message message = TaskManager.getInstance().askForCmds(token);
        if (message != null) {
            serverIO.response(message);
        }


        //依次执行任务
        //plate
//            Message res = new Message(Intent.TASK, PipConfigTask.class.getName());
//            Message res = new Message(Intent.TASK, TrainingTask.class.getName());

        //cigar
//            Message res = new Message(Intent.TASK, "zju.vipa.container.client.task.YoloCigarTask");


//            String cmds = "source /root/miniconda3/bin/activate clean_yolo " +
//                "&& python " + codePath + "/main.py";
        //写返回报文
//            response(new Message(Intent.SHELL_TASK, cmds));


    }

    /**
     * 发送到kafka
     */
    private void sendToKafka(Message message) {
        if (ClientRealTimeLogProducer.isActive) {
            //token要匹配
            if (ClientRealTimeLogProducer.client_token != null && ClientRealTimeLogProducer.client_token.equals(message.getToken())) {
                LogUtils.debug("sendToKafka: {}", message);
                ClientRealTimeLogProducer.send("key", message.getValue());
            }
        }
    }

    /**
     * 走handleRealtimeLog
     */
    @Deprecated
    private void shellBegin(Message message) {
        LogUtils.debug("{} shellBegin: {}", message.getTokenSuffix(), message.getValue());
//       sendToKafka(message);
    }

    /**
     * 走handleRealtimeLog
     */
    @Deprecated
    private void shellInfo(Message message) {
//        LogUtils.debug("Shell info from " + mSocket.getInetAddress() + " :" + value);
        LogUtils.debug("{} shellInfo: {}", message.getTokenSuffix(), message.getValue());
//        sendToKafka(message);
    }

    /**
     * shell指令执行结束
     */
    private void shellResult(Message message) {
        String result = message.getCustomData(Message.SHELL_RESULT_KEY);
        boolean isSuccess = false;
        if (Message.SHELL_RESULT_SUCCESS.equals(result)) {
            isSuccess = true;
        }
        TaskManager.getInstance().setLastShellResult(message.getToken(), isSuccess);

        if (Message.SHELL_RESULT_USER_STOPPED.equals(result)) {
            //TODO 通知前端关闭等待对话框
            //删除平台上记录的容器任务
            TaskManagerService.userStopTask(message.getToken());

        }
        LogUtils.debug("{} shellResult: {}", message.getTokenSuffix(), message.getValue());


//        Message toSendMsg = TaskManager.getInstance().askForCmds(message.getToken());
//        if (toSendMsg == null) {
//            /** 使容器开始抢新任务*/
//            toSendMsg = new ServerMessage(Intent.NULL);
//        }


        serverIO.response(new ServerMessage(Intent.ACK));
//        sendToKafka(message);
    }

    /**
     * 走handleRealtimeLog
     */
    @Deprecated
    private void shellError(Message message) {

//        LogUtils.error("{} shellError: {}", message.getTokenSuffix(), message.getValue());
        LogUtils.error("shellError: {}", message.getValue());
//        sendToKafka(message);
    }

    private void shellErrorHandle(Message message) {
        LogUtils.error("shellError: {}", message.getValue());
        /** 保存检测到的错误信息，放入对应client的task中暂存 */
        TaskManager.getInstance().handleError(message);
        /** 用于显示 */
        ManagementCenter.getInstance().updateLatestError(message.getToken(), message.getValue());
    }

    /**
     * 返回conda环境文件路径
     */
    private void getCondaEnvFileByTaskId(String taskId) {

        Message msg;
//        msg = new Message(Intent.condaEnvFileUrl, "/nfs2/mc/docker/aix-container/train_client.yml");

        msg = new ServerMessage(Intent.CONDA_ENV_FILE_URL, "/home/aix/code/train_client.yml");

        //写返回报文
        serverIO.response(msg);
    }

    /**
     * 返回pip环境文件路径
     */
    private void getPipEnvFileByTaskId(String taskId) {

        Message msg;
//        msg = new Message(Intent.condaEnvFileUrl, "/nfs2/mc/docker/aix-container/train_client.yml");

        String pipFile = "/home/aix/code/requirements.txt";
        msg = new ServerMessage(Intent.CONDA_ENV_FILE_URL, pipFile);

        //写返回报文
        serverIO.response(msg);
    }


}



