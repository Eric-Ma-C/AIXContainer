package org.zju.vipa.aix.container.center.network;

import io.netty.channel.ChannelHandlerContext;
import org.zju.vipa.aix.container.center.ManagementCenter;
import org.zju.vipa.aix.container.center.TaskManager;
import org.zju.vipa.aix.container.center.log.ClientLogFileManager;
import org.zju.vipa.aix.container.center.netty.NettyIoImpl;
import org.zju.vipa.aix.container.center.util.JwtUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.config.NetworkConfig;
import org.zju.vipa.aix.container.message.Intent;
import org.zju.vipa.aix.container.message.Message;
import org.zju.vipa.aix.container.message.SystemBriefInfo;
import org.zju.vipa.aix.container.utils.JsonUtils;

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
            case ASK_FOR_WORK:
                clientIdleAskForWork(msg.getToken());
                break;
            case CONDA_SOURCE:
                getCondaSource();
                break;
            case GET_CONDA_ENV_FILE_BY_TASKID:
                getCondaEnvFileByTaskId(msg.getValue());
                break;
            case GET_PIP_ENV_FILE_BY_TASKID:
                getPipEnvFileByTaskId(msg.getValue());
                break;
            case HEARTBEAT:
                handleHeartbeat(msg);
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
                registerContainer(msg.getValue());
                break;
            case GPU_INFO:
                handleGpuInfo(msg);
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
    private void clientIdleAskForWork(String token) {
        Message toSendMsg = TaskManager.getInstance().askForWork(token);
        if (toSendMsg == null) {
            /** 使容器开始心跳汇报 */
            toSendMsg = new ServerMessage(Intent.NULL);
        }
        serverIO.response(toSendMsg);
    }

    /**
     * 容器实时日志处理
     */
    private void handleRealtimeLog(Message msg) {
        LogUtils.debug("Realtime log from {}:{}", msg.getTokenSuffix(), msg.getValue());
    }

    /**
     * 处理gpu信息
     * 目前仅显示 todo 存入db?
     *
     * @param:
     * @return:
     */
    private void handleGpuInfo(Message message) {
        LogUtils.info("{}:\nGPU info:{}", message.getTokenSuffix(), message.getValue());
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
     *
     * @param token 容器上传的token，无法伪造
     * @return: void
     */
    private void registerContainer(String token) {
        boolean ok = JwtUtils.verify(token);
        Message msg;

        if (ok) {
            msg = new ServerMessage(Intent.REGISTER, "OK");
            //写返回报文
            serverIO.response(msg);
            /** 获取id号并缓存下来 */
            String id = ManagementCenter.getInstance().getIdByToken(token);

            LogUtils.info("Container id={} registered successfully! token={}", id, token);
        } else {
            msg = new ServerMessage(Intent.REGISTER, "DENIED");
            //写返回报文
            serverIO.response(msg);
            LogUtils.error("Container registered failed! token:{}", token);
        }

    }


    /**
     * 设置conda源
     *
     * @param:
     * @return:
     */
    private void getCondaSource() {
//        Message msg = new Message(Intent.CONDA_SOURCE,"test");
        Message msg = new ServerMessage(Intent.CONDA_SOURCE, NetworkConfig.DEFAULT_CONDA_SOURCE);

        //写返回报文
        serverIO.response(msg);
    }


    /**
     * 心跳消息处理
     * 平台有任务则下发任务执行
     * 否则继续心跳汇报
     *
     * @param msg
     * @return: void
     */
    private void handleHeartbeat(Message msg) {
        String token = msg.getToken();

        /** 根据token获取id */
        String id = ManagementCenter.getInstance().getIdByToken(token);

        if (id == null) {
            LogUtils.error("No such a device:{}", msg.getToken());
            return;
        }

        SystemBriefInfo info = JsonUtils.parseObject(msg.getValue(), SystemBriefInfo.class);
        if (info == null) {
            LogUtils.error("Heartbeat info error:{}", msg);
            return;
        }

        //todo mSocket 独立
//        LogUtils.info("{}:\nHeartbeats from client (id={}): IP{} CPU={}%  RAM={}%  time={}",
//            token, id, mSocket.getInetAddress(), info.getCpuRate(), info.getRamRate(),
//            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

        LogUtils.info("{}:\nHeartbeats from client (id={}): CPU={}%  RAM={}%  time={}",
            token, id, info.getCpuRate(), info.getRamRate(),
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));


        //尝试取出一条待发送消息
        Message message = TaskManager.getInstance().askForWork(token);
        boolean newTaskToExec = (message != null);


        if (newTaskToExec) {
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
            serverIO.response(message);

        }

    }

    private void shellBegin(Message message) {
        LogUtils.debug("{} shellBegin: {}", message.getTokenSuffix(), message.getValue());
    }

    private void shellInfo(Message message) {
//        LogUtils.debug("Shell info from " + mSocket.getInetAddress() + " :" + value);
        LogUtils.debug("{} shellInfo: {}", message.getTokenSuffix(), message.getValue());
    }


    /**
     * shell指令执行结果显示，环境配置错误处理
     *
     * @param message shell result info
     * @return: void
     */
    private void shellResult(Message message) {
        LogUtils.debug("{} shellResult: {}", message.getTokenSuffix(), message.getValue());
//        Message msg = null;
//        if (!"resultCode=0".equals(message.getValue())) {
//            /** 遇到错误尝试获取修复指令，可能会失败 */
//            msg = TaskManager.getInstance().askForWork(message.getToken());
//        }
//        //写返回报文
//        response(msg);
    }

    private void shellError(Message message) {

//        LogUtils.error("{} shellError: {}", message.getTokenSuffix(), message.getValue());
        LogUtils.error("shellError: {}", message.getValue());
    }

    private void shellErrorHandle(Message message) {
        shellError(message);
        /** 保存检测到的错误信息，放入对应client的task中暂存 */
        TaskManager.getInstance().handleError(message);
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



