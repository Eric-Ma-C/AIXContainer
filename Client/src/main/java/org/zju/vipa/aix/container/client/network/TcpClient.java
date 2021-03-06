package org.zju.vipa.aix.container.client.network;


import org.zju.vipa.aix.container.client.Client;
import org.zju.vipa.aix.container.client.netty.tcp.NettyIoImpl;
import org.zju.vipa.aix.container.client.task.BaseTask;
import org.zju.vipa.aix.container.client.task.ClientTaskController;
import org.zju.vipa.aix.container.client.task.custom.ClientShellTask;
import org.zju.vipa.aix.container.client.task.custom.DownloadDatasetTask;
import org.zju.vipa.aix.container.client.task.custom.DownloadModelTask;
import org.zju.vipa.aix.container.client.thread.ClientThreadManager;
import org.zju.vipa.aix.container.client.thread.GrabbingTaskThread;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.common.config.NetworkConfig;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.message.GpuInfo;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.message.SystemBriefInfo;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

/**
 * @Date: 2020/1/7 15:26
 * @Author: EricMa
 * @Description: 基于socket的tcp客户端
 */
public class TcpClient {
    /**
     * 网络IO接口
     */
    private ClientIO clientIO;

    private TcpClient() {
        /**  选择IO方式 */
        if (NetworkConfig.CLIENT_USE_NETTY) {
            clientIO = new NettyIoImpl();
        } else {
            clientIO = new SocketIoImpl();
        }

        if (ContainerTcpClientHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }
    }

    private static class ContainerTcpClientHolder {

        private static final TcpClient INSTANCE = new TcpClient();
    }

    public static TcpClient getInstance() {
        return ContainerTcpClientHolder.INSTANCE;
    }


    public void sendMessage(ClientMessage clientMessage) {
        clientIO.sendMessage(clientMessage);
    }

    /** 向平台通知任务执行完成 */
    public Message reportTaskFinished(int resultCode,String command) {
        ClientMessage msg = new ClientMessage(Intent.SHELL_RESULT, "resultCode=" + resultCode + " ,cmds=" + command);
        if (resultCode==0) {
            msg.addCustomData(Message.SHELL_RESULT_KEY,Message.SHELL_RESULT_SUCCESS);
        }else {
            msg.addCustomData(Message.SHELL_RESULT_KEY,Message.SHELL_RESULT_FAILED);
        }
        Message resMsg = clientIO.sendMsgAndGetResponse(msg);
        if (resMsg==null){
            ClientLogUtils.error("reportTaskFinished() get no response!");
        }
        return  resMsg;

    }

    /**
     * 处理平台下发的命令
     */

    private void handleResponseMsg(Message msg) {

        if (msg == null) {
            ClientLogUtils.worning("Response body is null!");
            return;
        }
        switch (msg.getIntent()) {
            case PONG:
                /** 心跳回应 */
                break;
            case NULL:
            case ACK:
                break;
            case YOU_CAN_GRAB_TASK:
                /** 平台没有待发送消息，容器开始抢任务 */
                ClientThreadManager.getInstance().startGrabbingTask();
                break;
            case TASK:
                execTask(msg.getValue());
                break;
            case SHELL_TASK:
                execShellTask(msg);
                break;
            case DOWNLOAD_MODEL:
                downloadModel(msg);
                break;
            case DOWNLOAD_DATASET:
                downloadDataset(msg);
                break;
            case REAL_TIME_LOG_SHOW_DETAIL:
                realTimeLogShowDetail();
                break;
            case REAL_TIME_LOG_SHOW_BRIEF:
                realTimeLogShowBrief();
                break;
            default:
                break;
        }
    }

    private void realTimeLogShowDetail() {
        ClientLogUtils.debug("Set Client.isUploadRealtimeLog=true");
        Client.isUploadRealtimeLog = true;
    }

    private void realTimeLogShowBrief() {
        ClientLogUtils.debug("Set Client.isUploadRealtimeLog=false");
        Client.isUploadRealtimeLog = false;
    }

    /**
     * 下载模型
     */
    private void downloadModel(Message msg) {
        ClientTaskController.getInstance().addTask(new DownloadModelTask(msg.getValue()));
    }

    /**
     * 下载模型
     */
    private void downloadDataset(Message msg) {
        ClientTaskController.getInstance().addTask(new DownloadDatasetTask(msg.getValue()));
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
    private void execShellTask(Message msg) {

        ClientShellTask task = new ClientShellTask(msg.getValue());
        String codePath = null;
        String modelArgs = null;
        if ((codePath = msg.getCustomData("codePath")) != null && (modelArgs = msg.getCustomData("modelArgs")) != null) {
            task.setTaskInfo(codePath, modelArgs);
            ClientLogUtils.info("Set codePath={} modelArgs={}", codePath, modelArgs);
        }

//        ClientLogUtils.debug("SeverCmdsTask="+value);
        ClientTaskController.getInstance().addTask(task);

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
//                task.setCodePath();
                ClientTaskController.getInstance().addTask(task);
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
        ClientMessage message = new ClientMessage(Intent.REGISTER, containerToken);


        Message response = clientIO.sendMsgAndGetResponse(message, 15 * 1000);

        String state = response == null ? "" : response.getValue();
        if ("OK".equals(state)) {
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

        ClientMessage message = new ClientMessage(Intent.GET_CONDA_ENV_FILE_BY_TASKID, taskId);
        String response = "";

        response = clientIO.sendMsgAndGetResponse(message).getValue();


        return response;

    }

    /**
     * 通过taskId获取pip配置文件连接
     *
     * @param: taskId
     * @return: pip配置文件下载url
     */

    public String getPipEnvFileByTaskId(String taskId) {

        ClientMessage message = new ClientMessage(Intent.GET_PIP_ENV_FILE_BY_TASKID, taskId);
        String response = "";

        response = clientIO.sendMsgAndGetResponse(message).getValue();


        return response;

    }

    /**
     * 重新设置conda国内源
     */

    public String getCondaSource() {
        ClientMessage message = new ClientMessage(Intent.CONDA_SOURCE);

        Message resMsg = clientIO.sendMsgAndGetResponse(message);
        if (resMsg == null) {
            return NetworkConfig.DEFAULT_CONDA_SOURCE;
        } else {
            return resMsg.getValue();
        }
    }

    /**
     * 向平台请求新的执行命令
     */

    public void askForCmds() {
        ClientMessage message = new ClientMessage(Intent.ASK_FOR_COMMENDS);

        Message resMsg = clientIO.sendMsgAndGetResponse(message);

        handleResponseMsg(resMsg);
    }


    /**
     * 向平台请求上传文件
     */
    public void requestUpload(String path, UploadDataListener listener) {
        ClientMessage message = new ClientMessage(Intent.REQUEST_UPLOAD, path);

        Message resMsg = clientIO.sendMsgAndGetResponse(message);

        if (resMsg == null || !Intent.UPLOAD_PERMITTED.match(resMsg)) {
            /** 服务器忙 */
            listener.onError(new AIXBaseException(ExceptionCodeEnum.UPLOAD_NOT_PERMITTED));
            return;
        }
        String serverWanted = resMsg.getValue();
        if (serverWanted != null || !"".equals(serverWanted)) {
            path = serverWanted;
        }
        clientIO.upLoadData(path, listener);

    }

    /**
     * 抢任务
     * 定时询问服务器有无连接需求，同时汇报容器状态（cpu，gpu，内存占用率等等）
     *
     * @param:
     * @return:
     */
    public boolean grabTask(SystemBriefInfo info) {

        ClientMessage message = new ClientMessage(Intent.GRAB_TASK, JsonUtils.toJSONString(info));
        Message resMsg = null;

        /** 5s读超时，抢任务并发多时可能会比较慢？ */
        resMsg = clientIO.sendMsgAndGetResponse(message, 5000);


        if (resMsg != null) {
            if (!Intent.GRAB_TASK_FAILED.match(resMsg)) {
                /** 失败次数归0 */
                Client.grabTaskFailedCount=0;
                ClientLogUtils.info("抢到任务:{}",resMsg.getIntent());
                /** 停止抢任务线程,执行任务 */
                GrabbingTaskThread.stop();
                handleResponseMsg(resMsg);
                return true;
            }
        }
        return false;
    }

    /**
     * 心跳汇报容器状态（cpu，gpu，内存占用率等等）
     * 上传gpu cuda和显存占用信息
     */
    public void heartbeatReport(GpuInfo info) {
        ClientMessage pingMsg = new ClientMessage(Intent.PING, JsonUtils.toJSONString(info));

        Message res = clientIO.sendMsgAndGetResponse(pingMsg, 2000);
        handleResponseMsg(res);

//        ClientLogUtils.debug("heartbeat recieve:{}",res);
    }

    /**
     * 不需要返回了，有askForWork
     */

    @Deprecated
    public void reportShellResult(ClientMessage msg) {
        Message body = null;

        /** 5000ms读超时 */
        body = clientIO.sendMsgAndGetResponse(msg, 5000);

        handleResponseMsg(body);
    }

//    /**
//     * 通过tcp连接,发送msg,获取响应信息
//     * 短连接方式
//     *
//     * @param: msg
//     * @return: 响应消息
//     */
//
//    private Message sendMsgAndGetResponse(ClientMessage msg) {
//        return sendMsgAndGetResponse(msg, NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);
//    }
//
//    /**
//     * 通过tcp连接,发送msg,获取响应信息
//     * 短连接方式
//     *
//     * @param: msg
//     * @return: 响应消息
//     */
//
//    private Message sendMsgAndGetResponse(ClientMessage msg, int readTimeOut) {
//        ClientLogUtils.debug("SEND:\n{}\n", msg);
//        Socket socket = null;
//        String response = null;
//        try {
//            //创建Socket对象
//            socket = new Socket(InetAddress.getByName(NetworkConfig.SERVER_IP), NetworkConfig.SERVER_PORT);
//            //设置读取超时时间
//            socket.setSoTimeout(readTimeOut);
//
//            byte[] sendBytes = JsonUtils.toJSONString(msg).getBytes(Message.CHARSET_NAME);
//
//            /** 发送数据 */
//            sendData(socket, sendBytes);
//
//            /** 通过shutdownOutput已经发送完数据，后续只能接受数据，半关闭 */
//            socket.shutdownOutput();
//
//            /** 写完以后进行读操作 */
//            response = new String(readData(socket), Message.CHARSET_NAME);
//
//        } catch (Exception e) {
//            ClientExceptionUtils.handle(e, false);
//        } finally {
//            /** 及时断开连接 */
//            try {
//                if (socket != null) {
//                    socket.close();
//                }
//            } catch (IOException e) {
//                ClientExceptionUtils.handle(e);
//            }
//        }
//
//        Message resMsg = new ClientMessage(Intent.NULL);
//        /** 服务端无需回应 */
//        if ("".equals(response) || response == null) {
//            return resMsg;
//        }
//        resMsg = JsonUtils.parseObject(response, Message.class);
//
//        ClientLogUtils.debug("GET RESPONSE:\n{}\n", resMsg);
//
//        return resMsg;
//    }
//
//    /**
//     * 通过tcp连接,发送msg,不接收响应
//     * 上传gpu信息，shell执行状态，java异常等
//     * 短连接方式
//     *
//     * @param: msg
//     */
//
//    public void sendMessage(ClientMessage msg) {
////        ClientLogUtils.debug("SEND:\n{}\n", msg);
//
//        Socket socket = null;
//        try {
//            /** 短连接方式 */
//            //创建Socket对象
//            socket = new Socket(InetAddress.getByName(NetworkConfig.SERVER_IP), NetworkConfig.SERVER_PORT);
//            //设置读取超时时间
//            socket.setSoTimeout(NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);
//
//            byte[] sendBytes = JsonUtils.toJSONString(msg).getBytes(Message.CHARSET_NAME);
//            /** 发送数据 */
//            sendData(socket, sendBytes);
//
//            /** 通过shutdownOutput已经发送完数据，后续只能接受数据，半关闭 */
//            socket.shutdownOutput();
//
//        } catch (Exception e) {
//            ClientExceptionUtils.handle(e, false);
//        } finally {
//            try {
//                if (socket != null) {
//                    socket.close();
//                }
//            } catch (IOException e) {
//                ClientExceptionUtils.handle(e);
//            }
//        }
//    }
//
//    /**
//     * 发送数据
//     * outputStream是socket中的，关闭socket会关闭它
//     */
//    private void sendData(Socket socket, byte[] data) {
//        try {
//            OutputStream outputStream = socket.getOutputStream();
//            int len = data.length;
//            //两字节数据段长度数据
//            outputStream.write(ByteUtils.intToByte4(len));
//            outputStream.write(data);
//            outputStream.flush();
//
//        } catch (IOException e) {
//            ClientExceptionUtils.handle(e);
//        }
//    }
//
//
//    /**
//     * 读返回字节流
//     */
//    private byte[] readData(Socket socket) {
//
//        byte[] reciveBytes = new byte[0];
//        try {
//            InputStream inputStream = socket.getInputStream();
//            ByteBuffer buffer = ByteBuffer.allocate(4);
//            inputStream.read(buffer.array(), 0, 4);
//            /** 字节流的长度 */
//            int len = buffer.getInt();
//            reciveBytes = new byte[len];
//            inputStream.read(reciveBytes, 0, len);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return reciveBytes;
//    }
//
//    /**
//     * 通过tcp连接,发送data,获取响应信息
//     *
//     * @return:
//     */
//    private boolean upLoadData(String path, UploadDataListener listener) {
//
//        InputStream dataInput = null;
//        try {
//            dataInput = new FileInputStream(path);
//        } catch (FileNotFoundException e) {
//            ClientLogUtils.info(true, "Uploading file {} is not exists.", path);
//            return false;
//        }
//        /** 读取文件到内存缓冲区 */
//        BufferedInputStream dataInputStream = new BufferedInputStream(dataInput);
//
//        String response = null;
//        Socket socket = null;
//        try {
//            //创建Socket对象
//            socket = new Socket(InetAddress.getByName(NetworkConfig.SERVER_IP), NetworkConfig.SERVER_PORT);
//            /** 设置读取超时时间,上传不容易，多等一下成功确认 */
//            socket.setSoTimeout(30 * 1000);
//
//            /** 先发一个Message */
//            Message msg = new ClientMessage(Intent.UPLOAD_DATA, path);
//            sendData(socket, JsonUtils.toJSONString(msg).getBytes(Message.CHARSET_NAME));
//
//
//            ClientLogUtils.info("开始上传文件：{}", path);
//            /** 发送缓冲区 */
//            BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
//
//            /** tcp MSS=1460 */
//            byte[] bys = new byte[1460];
//            int len;
//            while ((len = dataInputStream.read(bys)) != -1) {
//                outputStream.write(bys, 0, len);
//                outputStream.flush();
//            }
//            dataInputStream.close();
//            ClientLogUtils.info("文件{} 上传完毕，等待服务器确认", path);
//
//
//            /** 关闭socket输出流 */
//            socket.shutdownOutput();
//
//            // 读取反馈
//            response = new String(readData(socket), Message.CHARSET_NAME);
//
//        } catch (Exception e) {
//            ClientExceptionUtils.handle(e, false);
//        } finally {
//            /** 及时断开连接 */
//            try {
//                if (socket != null) {
//                    socket.close();
//                }
//            } catch (IOException e) {
//                ClientExceptionUtils.handle(e);
//            }
//        }
//        /** 服务端没有回应 */
//        if ("".equals(response) || response == null) {
////            listener.onError("上传完成后,服务端没有回应");
//            return false;
//        }
//
//        Message resMsg = JsonUtils.parseObject(response, Message.class);
//
//        if (resMsg.getIntent() == Intent.UPLOAD_SUCCESS) {
//            listener.onFinished();
//        }
//        return true;
//    }


//    public interface UploadDataListener {
//        /**
//         * 更新进度
//         */
//        void onProgress(int progress);
//
//        //        void onError(String msg);
//        void onFinished();
//    }


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


//    /**
//     * tcp连接是否保持
//     * 注意如果网络断开、服务器主动断开，
//     * Java底层是不会检测到连接断开并改变Socket的状态的，
//     * 所以，真实的检测连接状态还是得通过额外的手段
//     */
//    public boolean isSocketAvailable() {
//        return mSocket != null && mSocket.isBound() &&
//            !mSocket.isClosed() && mSocket.isConnected() &&
//            !mSocket.isInputShutdown() && !mSocket.isOutputShutdown();
//    }

}
