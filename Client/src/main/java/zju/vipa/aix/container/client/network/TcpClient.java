package zju.vipa.aix.container.client.network;


import zju.vipa.aix.container.client.task.BaseTask;
import zju.vipa.aix.container.client.task.TaskController;
import zju.vipa.aix.container.message.GpuInfo;
import zju.vipa.aix.container.message.SystemBriefInfo;
import zju.vipa.aix.container.client.task.SeverShellTask;
import zju.vipa.aix.container.client.thread.Heartbeat;
import zju.vipa.aix.container.network.NetworkConfig;
import zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.utils.JsonUtils;
import zju.vipa.aix.container.utils.LogUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @Date: 2020/1/7 15:26
 * @Author: EricMa
 * @Description: 容器端主动发起tcp请求 todo NIO
 */
public class TcpClient {


    /**
     * socket对象
     */
//    private Socket mSocket;
    /**
     * socket输出OutputStreamWriter
     */
//    private Writer mWriter;

    /**
     * socket输入
     */
//    BufferedReader mReader;
    private TcpClient() {
    }

    /**
     * 上传gpu cuda和显存占用信息
     *
     * @param:
     * @return:
     */
    public void uploadGpuInfo(GpuInfo gpuInfo) {
        Message msg = new Message(Intent.GPU_INFO, JsonUtils.toJSONString(gpuInfo));
        sendMessage(msg);
    }


    private static class ContainerTcpClientHolder {
        private static final TcpClient INSTANCE = new TcpClient();
    }

    public static TcpClient getInstance() {
        return ContainerTcpClientHolder.INSTANCE;
    }

    /**
     * tcp连接是否保持
     * 注意如果网络断开、服务器主动断开，
     * Java底层是不会检测到连接断开并改变Socket的状态的，
     * 所以，真实的检测连接状态还是得通过额外的手段
     */
//    public boolean isSocketAvailable() {
//        return mSocket != null && mSocket.isBound() &&
//            !mSocket.isClosed() && mSocket.isConnected() &&
//            !mSocket.isInputShutdown() && !mSocket.isOutputShutdown();
//    }

    /**
     * 处理平台下发的命令
     */
    public void handleResponseMsg(Message msg) {

        if (msg == null) {
            LogUtils.worning("Response body is null!");
            return;
        }
        switch (msg.getIntent()) {
//            case TASK_CODE_URL:
//                startNewTraining(msg.getValue());
//                break;
            case TASK:
                execTask(msg.getValue());
                break;
            case SHELL_TASK:
                execShellTask(msg.getValue());
                break;
            default:
                break;
        }
    }
///**
// *   开始执行新训练
// * @param codePath
// * @return:
// */
//    private void startNewTraining(String codePath) {
//
//
//        SeverShellTask task = new SeverShellTask(cmds);
//        ContainerController.getInstance().addTask(task);
//
//    }

    /**
     * 执行shell命令
     *
     * @param:
     * @return:
     */
    private void execShellTask(String value) {

        SeverShellTask task = new SeverShellTask(value);
        TaskController.getInstance().addTask(task);

    }

    /**
     * 执行task
     *
     * @param:
     * @return:
     */
    private void execTask(String taskName) {
        Class<?> taskClass = null;
        try {
            taskClass = Class.forName(taskName);
            if (taskClass != null) {
                BaseTask task = (BaseTask) taskClass.newInstance();
                TaskController.getInstance().addTask(task);
//                task.run();

                //得到方法
//                Method methlist[] = taskClass.getDeclaredMethods();
//                for (int i = 0; i < methlist.length; i++) {
//                    Method m = methlist[i];
//                }
                //获取到方法对象,method名为run
//                Method run = taskClass.getMethod("run");
                //执行方法
//                run.invoke(task);
            }
        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        }


    }


    /**
     * 首次运行需要向平台注册容器
     *
     * @param:
     * @return:
     */
    public boolean registerContainer(String containerToken) {
        Message message = new Message(Intent.REGISTER, containerToken);
        String response = "";
        try {
            response = sendMsgAndGetResponse(message).getValue();
        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        }
        if ("OK".equals(response)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过taskId获取conda配置文件连接
     *
     * @param: taskId
     * @return: conda配置文件下载url
     */
    public String getCondaEnvFileByTaskId(String taskId) {

        Message message = new Message(Intent.GET_CONDA_ENV_FILE_BY_TASKID, taskId);
        String response = "";
        try {
            response = sendMsgAndGetResponse(message).getValue();
        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        }

        return response;

    }

    /**
     * 通过taskId获取pip配置文件连接
     *
     * @param: taskId
     * @return: pip配置文件下载url
     */
    public String getPipEnvFileByTaskId(String taskId) {

        Message message = new Message(Intent.GET_PIP_ENV_FILE_BY_TASKID, taskId);
        String response = "";
        try {
            response = sendMsgAndGetResponse(message).getValue();
        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        }

        return response;

    }

    /**
     * 重新设置conda国内源
     */
    public String getCondaSource() {
        Message message = new Message(Intent.CONDA_SOURCE);

        Message resMsg = sendMsgAndGetResponse(message);
        if (resMsg == null) {
            return NetworkConfig.DEFAULT_CONDA_SOURCE;
        } else {
            return resMsg.getValue();
        }
    }

    /**
     * 心跳汇报
     * 定时询问服务器有无连接需求，同时汇报容器状态（cpu，gpu，内存占用率等等）
     *
     * @param:
     * @return:
     */
    public void heartbeatReport(SystemBriefInfo info) {

        Message message = new Message(Intent.HEARTBEAT, JsonUtils.toJSONString(info));
        Message body = null;
        try {
            /** 1000ms读超时 */
            body = sendMsgAndGetResponse(message, 1000);
        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        }

        if (body != null && body.getIntent() != Intent.NULL) {
            /** 停止心跳线程,执行任务 */
            Heartbeat.stop();
            handleResponseMsg(body);
        }

    }

    public void reportShellResult(Message msg) {
        Message body = null;
        try {
            /** 5000ms读超时 */
            body = sendMsgAndGetResponse(msg, 5000);
        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        }

        handleResponseMsg(body);


    }

    /**
     * 通过tcp连接,发送msg,获取响应信息
     * 短连接方式
     *
     * @param: msg
     * @return: 响应消息
     */
    private Message sendMsgAndGetResponse(Message msg) {
        return sendMsgAndGetResponse(msg, NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);
    }

    /**
     * 通过tcp连接,发送msg,获取响应信息
     * 短连接方式
     *
     * @param: msg
     * @return: 响应消息
     */
    private Message sendMsgAndGetResponse(Message msg, int readTimeOut) {
        StringBuffer response = new StringBuffer();

        Writer mWriter = null;
        Socket mSocket = null;
        BufferedReader mReader = null;
        try {
            /** 短连接方式 */

            //创建Socket对象

            mSocket = new Socket(InetAddress.getByName(NetworkConfig.SERVER_IP), NetworkConfig.SERVER_PORT);


            //设置读取超时时间
            mSocket.setSoTimeout(readTimeOut);


//            }

            mWriter = new OutputStreamWriter(mSocket.getOutputStream(), Message.CHARSET_NAME);

            mWriter.write(JsonUtils.toJSONString(msg));
            mWriter.write(Message.END_STRING + "\n");
            mWriter.flush();

            //通过shutdownOutput已经发送完数据，后续只能接受数据
//            mSocket.shutdownOutput();

            //写完以后进行读操作

            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), Message.CHARSET_NAME));

            String tmpStr;
            int index;
            try {
                while ((tmpStr = mReader.readLine()) != null) {
                    if ((index = tmpStr.indexOf(Message.END_STRING)) != -1) {
                        response.append(tmpStr.substring(0, index));
                        break;
                    }
                    response.append(tmpStr);
                }
            } catch (SocketTimeoutException e) {
                ClientExceptionUtils.handle(e, "响应数据读取超时。");
            }
        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        } finally {
            /** 及时断开连接 */
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
                ClientExceptionUtils.handle(e);
            }
        }

        Message resMsg = new Message(Intent.NULL);
        /** 服务端无需回应 */
        if ("".equals(response) || response == null) {
            return resMsg;
        }

        LogUtils.debug("Client received: " + response);

        resMsg = JsonUtils.parseObject(response.toString(), Message.class);

        return resMsg;
    }

    /**
     * 通过tcp连接,发送msg,不接收响应
     * 上传gpu信息，shell执行状态，java异常等
     * 短连接方式
     *
     * @param: msg
     */
    public void sendMessage(Message msg) {
        StringBuffer response = new StringBuffer();

        Writer mWriter = null;
        Socket mSocket = null;
        try {
            /** 短连接方式 */
            //创建Socket对象
            mSocket = new Socket(InetAddress.getByName(NetworkConfig.SERVER_IP), NetworkConfig.SERVER_PORT);

            //设置读取超时时间
//            mSocket.setSoTimeout(5000);

            mWriter = new OutputStreamWriter(mSocket.getOutputStream(), Message.CHARSET_NAME);

            mWriter.write(JsonUtils.toJSONString(msg));
            mWriter.write(Message.END_STRING + "\n");
            mWriter.flush();


        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        } finally {
            try {
                if (mWriter != null) {
                    mWriter.close();
                }
                if (mSocket != null) {
                    mSocket.close();
                }
            } catch (IOException e) {
                ClientExceptionUtils.handle(e);
            }
        }


    }


    /**
     * 断开tcp连接
     */
//    private void disconnect(Writer mWriter,BufferedReader mReader,Socket mSocket) {
//
//
//    }
//    /**
//     * 发起tcp长连接,直到主动断开连接,或服务器,网络异常断开
//     */
//    private String connectKeepAlive(MessageBody messageBody) throws Exception {
//        //创建Socket对象
//        Socket socket;
//        if (DebugUtils.isLocalDebug) {
//            socket = new Socket(InetAddress.getByName(DEBUG_SERVER_IP), SERVER_PORT);
//        } else {
//            socket = new Socket(InetAddress.getByName(VIPA_ALIYUN_SERVER_IP), SERVER_PORT);
//        }
//
//        //与服务端建立连接
//        //建立连接后就可以往服务端写数据了
//        Writer writer = new OutputStreamWriter(socket.getOutputStream(), CHARSET_NAME);
//        writer.write(JsonUtils.toJSONString(messageBody));
//        writer.write(END_STRING + "\n");
//        writer.flush();
//
//
//        //写完以后进行读操作
//        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET_NAME));
//        //设置超时间
//        socket.setSoTimeout(SOCKET_TIMEOUT);
//        StringBuffer sb = new StringBuffer();
//        String temp;
//        int index;
//        try {
//            while ((temp = br.readLine()) != null) {
//                if ((index = temp.indexOf(END_STRING)) != -1) {
//                    sb.append(temp.substring(0, index));
//                    break;
//                }
//                sb.append(temp);
//            }
//        } catch (SocketTimeoutException e) {
//            ExceptionUtils.handle(e, "数据读取超时。");
//        }
//
//
////        System.out.println("服务端: " + sb);
//
//        writer.close();
//        br.close();
//        socket.close();
//
//        return sb.toString();
//    }


}