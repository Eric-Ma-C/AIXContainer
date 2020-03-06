package zju.vipa.container.network;


import zju.vipa.container.utils.ExceptionUtils;
import zju.vipa.container.message.Intent;
import zju.vipa.container.message.Message;
import zju.vipa.container.utils.DebugUtils;
import zju.vipa.container.utils.JsonUtils;
import zju.vipa.container.utils.LogUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @Date: 2020/1/7 15:26
 * @Author: EricMa
 * @Description: 容器端主动发起tcp请求
 */
public class TcpClient {


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

    private TcpClient() {
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
    public boolean isSocketAvailable() {
        return mSocket != null && mSocket.isBound() &&
            !mSocket.isClosed() && mSocket.isConnected() &&
            !mSocket.isInputShutdown() && !mSocket.isOutputShutdown();
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
            ExceptionUtils.handle(e);
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
            ExceptionUtils.handle(e);
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
            return "";
        } else {
            return resMsg.getValue();
        }
    }

    /**
     * 上传容器指令执行输出和结果
     * java异常等等
     * @param:
     * @return:
     */
    public Message uploadState(Intent intent, String value) {

        Message message = new Message(intent, value);
        Message body = null;
        try {
            body = sendMsgAndGetResponse(message);
        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }

        return body;

    }



    /**
     * tcp断开后,守护进程间隔性发起的tcp连接,
     * 询问服务器是否有消息传达
     *
     * @param:
     * @return:
     */
    public void deamonQuery() {

        Message message = new Message(Intent.DEAMON_QUERY, "");
        try {
            sendMsgAndGetResponse(message);
        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }

        if (isSocketAvailable()) {
            //todo
        }

    }

    /**
     *
     */
    /**
     * 通过tcp连接,发送msg,获取响应信息
     * 短连接方式
     *
     * @param: msg
     * @return: 响应消息
     */
    private Message sendMsgAndGetResponse(Message msg) {
        StringBuffer response = new StringBuffer();

        try {
            /** 短连接方式 */
//            if (!isSocketAvailable()) {
            //创建Socket对象
            if (DebugUtils.isLocalDebug) {
                mSocket = new Socket(InetAddress.getByName(NetworkConfig.DEBUG_SERVER_IP), NetworkConfig.SERVER_PORT);
            } else {
                mSocket = new Socket(InetAddress.getByName(NetworkConfig.MY_ALIYUN_SERVER_IP), NetworkConfig.SERVER_PORT);
            }
            //设置读取超时时间
            mSocket.setSoTimeout(NetworkConfig.SOCKET_READ_TIMEOUT);


//            }

            //todo 检测writer,reader状态
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
                ExceptionUtils.handle(e, "响应数据读取超时。");
            }
        } catch (Exception e) {
            ExceptionUtils.handle(e);
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
