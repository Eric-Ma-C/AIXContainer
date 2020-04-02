package zju.vipa.aix.container.center.network;

import zju.vipa.aix.container.center.ManagementCenter;
import zju.vipa.aix.container.center.TaskManager;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.message.SystemBriefInfo;
import zju.vipa.aix.container.config.NetworkConfig;
import zju.vipa.aix.container.center.util.ExceptionUtils;
import zju.vipa.aix.container.utils.JsonUtils;
import zju.vipa.aix.container.center.util.JwtUtils;
import zju.vipa.aix.container.center.util.LogUtils;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date: 2020/1/7 16:10
 * @Author: EricMa
 * @Description: 用来处理接收到的Socket请求
 */
public class SocketHandler implements Runnable {

    /**
     * socket对象
     */
    private Socket mSocket;
    /**
     * socket输出OutputStreamWriter
     */
    private Writer mWriter;
    /**
     * socket输入
     */
    BufferedReader mReader;

    public SocketHandler(Socket mSocket) {
        this.mSocket = mSocket;
    }

    @Override
    public void run() {

        handleRequest();

    }

    /**
     * 处理一次Socket请求
     * (短连接)
     *
     * @throws Exception
     */
    private void handleRequest() {

        Message msg = readRequests();

        if (msg == null) {
            LogUtils.worning("Requests body is null!");
            return;
        }
        switch (msg.getIntent()) {
            case ASK_FOR_WORK:
                clientAskForWork(msg.getToken());
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
            default:
                break;
        }


        /** 关闭socket,短连接 */
        disconnect();
    }

    /**
     * 容器已没有待执行Task
     */
    private void clientAskForWork(String token) {
        Message toSendMsg=TaskManager.getInstance().askForWork(token);
        if (toSendMsg==null){
            /** 使容器开始心跳汇报 */
           toSendMsg=new ServerMessage(Intent.NULL);
        }
        response(toSendMsg);
    }

    /**
     * 容器实时日志处理
     */
    private void handleRealtimeLog(Message msg) {
        LogUtils.debug(msg);
    }

    /**
     * 处理gpu信息
     * 目前仅显示 todo 存入db?
     *
     * @param:
     * @return:
     */
    private void handleGpuInfo(Message message) {
        LogUtils.info(message.getToken(), "GPU info:" + message.getValue());
    }

    /**
     * 显示异常信息
     *
     * @param:
     * @return:
     */
    private void handleException(Message message) {
        LogUtils.error(message);
    }


    /**
     * 首次连接平台，验证容器的token
     *
     * @param token 容器上传的token，无法伪造
     * @return: void
     */
    private void registerContainer(String token) {
        boolean ok = JwtUtils.verify(token);
        Message msg = new ServerMessage(Intent.REGISTER, "DENIED");

        if (ok) {
            /** 获取id号并缓存下来 */
            String id = ManagementCenter.getInstance().getIdByToken(token);

            msg = new ServerMessage(Intent.REGISTER, "OK");
            LogUtils.info("Container " + id + " registered successfully! token=" + token);
        } else {
            LogUtils.error("Container registered failed! token:" + token);
        }
        //写返回报文
        response(msg);
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
    public void disconnect() {

        try {
            if (mWriter != null) {
                mWriter.close();
            }
            if (mReader != null) {
                mReader.close();
            }
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
            LogUtils.error("No such a device:" + msg);
            disconnect();
            return;
        }

        SystemBriefInfo info = JsonUtils.parseObject(msg.getValue(), SystemBriefInfo.class);
        if (info == null) {
            LogUtils.error("Heartbeat info error:" + msg);
            disconnect();
            return;
        }


        LogUtils.info(token, "Heartbeats from client (id=" + id + "): IP" + mSocket.getInetAddress() + " CPU=" + info.getCpuRate() +
            "%  RAM=" + info.getRamRate() + "%  time=" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));


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
        LogUtils.info(message.getToken(), "\n****************************\nexec: " + message.getValue() + "\n****************************");
    }

    private void shellInfo(Message message) {
//        LogUtils.debug("Shell info from " + mSocket.getInetAddress() + " :" + value);
        LogUtils.info(message);
    }


    /**
     * shell指令执行结果显示，环境配置错误处理
     *
     * @param message shell result info
     * @return: void
     */
    private void shellResult(Message message) {
        LogUtils.info(message);
        Message msg = null;
        if (!"resultCode=0".equals(message.getValue())) {
            /** 遇到错误尝试获取修复指令，可能会失败 */
            msg = TaskManager.getInstance().askForWork(message.getToken());
        }
        //写返回报文
        response(msg);
    }

    private void shellError(Message message) {
        LogUtils.error(message);
        /** 保存检测到的错误信息，放入对应client的task中暂存 */
        TaskManager.getInstance().handleError(message);

    }

    /**
     * 返回conda环境文件路径
     */
    private void getCondaEnvFileByTaskId(String taskId) {

        Message msg;
//        msg = new Message(Intent.condaEnvFileUrl, "/nfs2/mc/docker/aix-container/train_client.yml");

        msg = new ServerMessage(Intent.CONDA_ENV_FILE_URL, "/root/aix/code/train_client.yml");

        //写返回报文
        response(msg);
    }

    /**
     * 返回pip环境文件路径
     */
    private void getPipEnvFileByTaskId(String taskId) {

        Message msg;
//        msg = new Message(Intent.condaEnvFileUrl, "/nfs2/mc/docker/aix-container/train_client.yml");

        String pipFile = "/root/aix/code/requirements.txt";
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
        StringBuilder response = new StringBuilder();

        try {
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), Message.CHARSET_NAME));

            String temp;
            int index;
            while ((temp = mReader.readLine()) != null) {
                /** 遇到eof时就结束接收 */
                //todo 改为读取前两个字节为报文长度,确定结束位置
                if ((index = temp.indexOf(Message.END_STRING)) != -1) {
                    response.append(temp.substring(0, index));
                    break;
                }
                response.append(temp);
            }
        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }
//        LogUtils.debug("Receive from client " + mSocket.getInetAddress() + ":" + mSocket.getPort() + " :" + response);
//        LogUtils.info(mSocket.getInetAddress() + ":" + response);

        Message msg = JsonUtils.parseObject(response.toString(), Message.class);
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
        OutputStream outputStream = null;
        try {
            outputStream = mSocket.getOutputStream();

            mWriter = new OutputStreamWriter(outputStream, Message.CHARSET_NAME);
            mWriter.write(JsonUtils.toJSONString(msg));
//                writer.write("hello，client      server time:"+ TimeUtils.getTimeStr());
            mWriter.write(Message.END_STRING + "\n");
            mWriter.flush();

        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }
    }
}



