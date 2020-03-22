package zju.vipa.aix.container.center.network;

import zju.vipa.aix.container.center.db.DbManager;
import zju.vipa.aix.container.center.env.EnvError;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.message.SystemBriefInfo;
import zju.vipa.aix.container.network.NetworkConfig;
import zju.vipa.aix.container.utils.ExceptionUtils;
import zju.vipa.aix.container.utils.JsonUtils;
import zju.vipa.aix.container.utils.JwtUtil;
import zju.vipa.aix.container.utils.LogUtils;

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
        try {
            handleRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            case EXCEPTION:
                handleException(msg.getValue());
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
                handleHeartbeat(msg.getValue());
                break;
            case SHELL_BEGIN:
                shellBegin(msg.getValue());
                break;
            case SHELL_INFO:
                shellInfo(msg.getValue());
                break;
            case SHELL_RESULT:
                shellResult(msg.getValue());
                break;
            case SHELL_ERROR:
                shellError(msg.getValue());
                break;
            case REGISTER:
                registerContainer(msg.getValue());
                break;
            case GPU_INFO:
                handleGpuInfo(msg.getValue());
                break;
            default:
                break;
        }


        /** 关闭socket,短连接 */
        disconnect();
    }

    /**
     * 处理gpu信息
     * 目前仅显示 todo 存入db
     *
     * @param:
     * @return:
     */
    private void handleGpuInfo(String value) {
        LogUtils.info(value);
    }

    /**
     * 显示异常信息
     *
     * @param:
     * @return:
     */
    private void handleException(String value) {
        LogUtils.error(value);
    }


    /**
     * 首次连接平台，验证容器的token
     *
     * @param token 容器上传的token，无法伪造
     * @return: void
     */
    private void registerContainer(String token) {
        boolean ok = JwtUtil.verify(token);
        Message msg = new Message(Intent.REGISTER, "DENIED");
        if (ok) {
            msg = new Message(Intent.REGISTER, "OK");
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
        Message msg = new Message(Intent.CONDA_SOURCE, NetworkConfig.DEFAULT_CONDA_SOURCE);

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
    private void handleHeartbeat(String msg) {
        //根据容器id判断是否需要连接容器
        //尝试取出一条待发送消息
//        Message message = MessageManager.getInstance().getMessageById(NetworkConfig.TEST_CONTAINER_ID);
//        boolean needTcpConnect = (message != null);

        String codePath = DbManager.getInstance().grabTask(NetworkConfig.TEST_CONTAINER_ID);
        boolean newTaskToExec = (codePath != null);


        if (newTaskToExec) {
            //依次执行任务
            //plate
//            Message res = new Message(Intent.TASK, PipConfigTask.class.getName());
//            Message res = new Message(Intent.TASK, TrainingTask.class.getName());

            //cigar
//            Message res = new Message(Intent.TASK, "zju.vipa.container.client.task.YoloCigarTask");
            String cmds = "conda env create -f " + codePath + "/environment.yaml " +
                "&& source /root/miniconda3/bin/activate clean_yolo " +
                "&& python " + codePath + "/main.py";
            //写返回报文
            response(new Message(Intent.SHELL_TASK, cmds));
//            response(new Message(Intent.TASK_CODE_URL,codePath));

        } else {
            SystemBriefInfo info = JsonUtils.parseObject(msg, SystemBriefInfo.class);
            if (info != null) {
                LogUtils.info("Heartbeats from " + mSocket.getInetAddress() + ": CPU=" + info.getCpuRate() +
                    "%  RAM=" + info.getRamRate() + "%      time=" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            }
            //todo 不响应，节省开销
//            Message msg = new Message(Intent.NULL);
            disconnect();
        }
    }

    private void shellBegin(String value) {
        LogUtils.info("\n****************************\nexec: " + value + "\n****************************");
    }

    private void shellInfo(String value) {
//        LogUtils.debug("Shell info from " + mSocket.getInetAddress() + " :" + value);
        LogUtils.info(value);
    }


    /**
     * shell指令执行结果显示，环境配置错误处理
     *
     * @param value shell result info
     * @return: void
     */
    private void shellResult(String value) {
        LogUtils.info(value);
        Message res = new Message(Intent.NULL, "");
        if (!"resultCode=0".equals(value) && EnvError.errorDetected) {
            EnvError.errorDetected = false;
            LogUtils.info("Solve ModuleNotFoundError...");
            //安装缺少的包
            res = new Message(Intent.SHELL_TASK, EnvError.cmds);
        }
        //写返回报文
        response(res);
    }

    private void shellError(String value) {
        LogUtils.error(value);
        //自动安装一些conda库
        if (value != null && value.startsWith("ModuleNotFoundError: No module named")) {
            String moduleName = value.substring(value.indexOf("named") + 7, value.length() - 1);
            EnvError.cmds = "source /root/miniconda3/bin/activate clean_yolo && pip install " + moduleName + " && python /nfs2/sontal/codes/TrainerProxy/main.py";

            EnvError.errorDetected = true;
        }
    }

    /**
     * 返回conda环境文件路径
     */
    private void getCondaEnvFileByTaskId(String taskId) {

        Message msg;
//        msg = new Message(Intent.condaEnvFileUrl, "/nfs2/mc/docker/aix-container/train_client.yml");

        msg = new Message(Intent.CONDA_ENV_FILE_URL, "/root/aix/code/train_client.yml");

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
        msg = new Message(Intent.CONDA_ENV_FILE_URL, pipFile);

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



