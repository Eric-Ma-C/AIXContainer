package zju.vipa.aix.container.center.network;

import zju.vipa.aix.container.center.ManagementCenter;
import zju.vipa.aix.container.center.TaskManager;
import zju.vipa.aix.container.center.log.ClientLogManager;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.message.SystemBriefInfo;
import zju.vipa.aix.container.config.NetworkConfig;
import zju.vipa.aix.container.center.util.ExceptionUtils;
import zju.vipa.aix.container.utils.ByteUtils;
import zju.vipa.aix.container.utils.JsonUtils;
import zju.vipa.aix.container.center.util.JwtUtils;
import zju.vipa.aix.container.center.util.LogUtils;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date: 2020/1/7 16:10
 * @Author: EricMa
 * @Description: 处理接收到的所有容器请求
 */
public class SocketHandler implements Runnable {

    /**
     * socket对象
     */
    private Socket mSocket;

    public SocketHandler(Socket mSocket) {
        this.mSocket = mSocket;
    }

    @Override
    public void run() {

        handle();

    }

    /**
     * 处理一次Socket请求
     * (短连接)
     *
     * @throws Exception
     */
    private void handle() {
//        LogUtils.debug("handle()");
        Message msg = readRequests();

        if (msg == null) {
            LogUtils.worning("Requests body is null!");
            disconnect();
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
                isAcceptUpload();
                break;
            case UPLOAD_DATA:
                saveData(msg);
                break;
            default:
                break;
        }

        /** 关闭socket,短连接方式 */
        disconnect();
    }

    /**
     * 保存数据
     */
    private void saveData(Message msg) {

        File saveFile = ClientLogManager.getInstence().getSavePath(msg.getToken(), msg.getValue());

        try {
            // 封装通道内流
            BufferedInputStream inputStream = new BufferedInputStream(mSocket.getInputStream());
            // 封装文件
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));

            byte[] bys = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(bys)) != -1) {
                bos.write(bys, 0, len);
                bos.flush();
            }

//            bis.close();
            bos.close();
        } catch (IOException e) {
            ExceptionUtils.handle(e);
        }

        LogUtils.info("文件{}接收成功。", saveFile.getPath());
        // 反馈上传成功
        response(new ServerMessage(Intent.UPLOAD_SUCCESS));

    }

    /**
     * 是否接受上传
     */
    private void isAcceptUpload() {
        response(new ServerMessage(Intent.UPLOAD_PERMITTED,ClientLogManager.getInstence().getFilePathByDate("2020-04-19")));
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
        response(toSendMsg);
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
        LogUtils.error("{}:\n{}" , message.getTokenSuffix(),message.getValue());
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
            response(msg);
            /** 获取id号并缓存下来 */
            String id = ManagementCenter.getInstance().getIdByToken(token);

            LogUtils.info("Container id={} registered successfully! token={}", id, token);
        } else {
            msg = new ServerMessage(Intent.REGISTER, "DENIED");
            //写返回报文
            response(msg);
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
        response(msg);
    }

    /**
     * 断开tcp连接
     */
    private void disconnect() {

        try {
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (IOException e) {
            ExceptionUtils.handle(e);
        }
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
            disconnect();
            return;
        }

        SystemBriefInfo info = JsonUtils.parseObject(msg.getValue(), SystemBriefInfo.class);
        if (info == null) {
            LogUtils.error("Heartbeat info error:{}", msg);
            disconnect();
            return;
        }


        LogUtils.info("{}:\nHeartbeats from client (id={}): IP{} CPU={}%  RAM={}%  time={}",
            token, id, mSocket.getInetAddress(), info.getCpuRate(), info.getRamRate(),
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
            response(message);

        } else {
            /** 服务器对心跳包不响应，节省开销 */
            disconnect();
//            response(new ServerMessage(Intent.NULL));
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

        LogUtils.error("{} shellError: {}", message.getTokenSuffix(), message.getValue());

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
        response(msg);
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
        response(msg);
    }

    /**
     * 读取发送过来的tcp请求报文
     *
     * @param:
     * @return: 可能为null
     */
    private Message readRequests() {
        String request = null;

        try {
            request = new String(readData(mSocket), Message.CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            ExceptionUtils.handle(e);
        }

        Message msg = JsonUtils.parseObject(request, Message.class);
        return msg;
    }

    /**
     * 写返回消息
     *
     * @param: msg
     * @return:
     */
    private void response(Message msg) {
        if (msg == null) {
            LogUtils.worning("Response messags is null!");
            return;
        }

//        LogUtils.info("SEND RESPONSE:{}",msg);
        try {
            sendData(mSocket, JsonUtils.toJSONString(msg).getBytes(Message.CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            ExceptionUtils.handle(e);
        }

    }

    /**
     * 返回数据
     */
    private void sendData(Socket socket, byte[] data) {
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            int len = data.length;
            //两字节数据段长度数据
            outputStream.write(ByteUtils.intToByte4(len));
            outputStream.write(data);
            outputStream.flush();

        } catch (IOException e) {
            ExceptionUtils.handle(e);
        }
    }


    /**
     * 读请求字节流
     */
    private byte[] readData(Socket socket) {

        byte[] reciveBytes = new byte[0];
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            ByteBuffer buffer = ByteBuffer.allocate(4);
            inputStream.read(buffer.array(), 0, 4);
            /** 字节流的长度 */
            int len = buffer.getInt();
            reciveBytes = new byte[len];
            inputStream.read(reciveBytes, 0, len);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return reciveBytes;
    }
}



