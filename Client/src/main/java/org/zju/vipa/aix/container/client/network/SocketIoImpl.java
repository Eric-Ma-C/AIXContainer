package org.zju.vipa.aix.container.client.network;

import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.config.NetworkConfig;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.utils.ByteUtils;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @Date: 2020/5/6 17:56
 * @Author: EricMa
 * @Description: socket实现tcp发送和接收
 */
public class SocketIoImpl implements ClientIO {

    @Override
    public Message sendMsgAndGetResponse(ClientMessage msg) {
        return sendMsgAndGetResponse(msg, NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);
    }

    /**
     * 同步阻塞方法
     *
     * @param message     待发送消息
     * @param readTimeOut 超时时间
     * @return: zju.vipa.aix.container.message.Message
     */
    @Override
    public Message sendMsgAndGetResponse(ClientMessage message, int readTimeOut) {
        if (DebugConfig.IS_LOCAL_DEBUG) {
            readTimeOut = DebugConfig.SOCKET_READ_TIMEOUT_DEBUG;
        }
        if (DebugConfig.CLIENT_NETWORK_IO_LOG) {
            ClientLogUtils.debug("SEND:\n{}\n", message);
        }
        Socket socket = null;
        String response = null;
        try {
            //创建Socket对象
            socket = new Socket(InetAddress.getByName(NetworkConfig.SERVER_IP), NetworkConfig.SERVER_PORT_LISTENING);
            //设置读取超时时间
            socket.setSoTimeout(readTimeOut);

            byte[] sendBytes = JsonUtils.toJSONString(message).getBytes(Message.CHARSET_NAME);

            /** 发送数据 */
            sendData(socket, sendBytes);

            /** 通过shutdownOutput已经发送完数据，后续只能接受数据，半关闭 */
            socket.shutdownOutput();

            /** 写完以后进行读操作 */
            response = new String(readData(socket), Message.CHARSET_NAME);

        } catch (Exception e) {
            ClientExceptionUtils.handle(e, false);
        } finally {
            /** 及时断开连接 */
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                ClientExceptionUtils.handle(e);
            }
        }

        Message resMsg = new ClientMessage(Intent.NULL);
        /** 服务端无需回应 */
        if ("".equals(response) || response == null) {
            return resMsg;
        }
        resMsg = JsonUtils.parseObject(response, Message.class);

        if (DebugConfig.CLIENT_NETWORK_IO_LOG) {
            ClientLogUtils.debug("GET RESPONSE:\n{}\n", resMsg);
        }

        return resMsg;
    }


    @Override
    public void sendMessage(ClientMessage message) {

        if (!Intent.SHELL_ERROR_HELP.match(message) &&
            !Intent.EXCEPTION.match(message) &&
            !Intent.REAL_TIME_LOG.match(message)
            && DebugConfig.CLIENT_NETWORK_IO_LOG) {
            ClientLogUtils.debug("SEND:\n{}\n", message);
        }

        Socket socket = null;
        try {
            /** 短连接方式 */
            //创建Socket对象
            socket = new Socket(InetAddress.getByName(NetworkConfig.SERVER_IP), NetworkConfig.SERVER_PORT_LISTENING);
            //设置读取超时时间
            socket.setSoTimeout(NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);

            byte[] sendBytes = JsonUtils.toJSONString(message).getBytes(Message.CHARSET_NAME);
            /** 发送数据 */
            sendData(socket, sendBytes);

            /** 通过shutdownOutput已经发送完数据，后续只能接受数据，半关闭 */
            socket.shutdownOutput();

        } catch (Exception e) {
            ClientExceptionUtils.handle(e, false);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                ClientExceptionUtils.handle(e);
            }
        }
    }

    /**
     * 发送数据
     * outputStream是socket中的，关闭socket会关闭它
     */
    private void sendData(Socket socket, byte[] data) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            int len = data.length;
            //两字节数据段长度数据
            outputStream.write(ByteUtils.intToByte4(len));
            outputStream.write(data);
            outputStream.flush();

        } catch (IOException e) {
            ClientExceptionUtils.handle(e);
        }
    }


    /**
     * 读返回字节流
     */
    private byte[] readData(Socket socket) {

        byte[] reciveBytes = new byte[0];
        try {
            InputStream inputStream = socket.getInputStream();
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

    /**
     * 通过tcp连接,发送data,获取响应信息
     *
     * @return:
     */
    @Override
    public void upLoadData(String path, UploadDataListener listener) {

        InputStream dataInput = null;
        try {
            dataInput = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            ClientLogUtils.info(true, "Uploading file {} is not exists.", path);
            listener.onError(e);
            return;
        }
        /** 读取文件到jvm堆内存中缓冲 */
        BufferedInputStream dataInputStream = new BufferedInputStream(dataInput);

        String response = null;
        Socket socket = null;
        try {
            //创建Socket对象
            socket = new Socket(InetAddress.getByName(NetworkConfig.SERVER_IP), NetworkConfig.SERVER_PORT_LISTENING);
            /** 设置读取超时时间,上传不容易，多等一下成功确认 */
            socket.setSoTimeout(30 * 1000);

            /** 先发一个Message */
            Message msg = new ClientMessage(Intent.UPLOAD_DATA, path);
            sendData(socket, JsonUtils.toJSONString(msg).getBytes(Message.CHARSET_NAME));


            ClientLogUtils.info(true,"开始上传文件：{}", path);
            /** 发送缓冲区 */
            BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

            /** tcp MSS=1460 */
            byte[] bys = new byte[1460];
            int len;
            while ((len = dataInputStream.read(bys)) != -1) {
                outputStream.write(bys, 0, len);
                outputStream.flush();
            }
            dataInputStream.close();
            ClientLogUtils.info("文件{} 上传完毕，等待服务器确认", path);


            /** 关闭socket输出流，通知服务器发送完了 */
            socket.shutdownOutput();

            // 读取反馈
            response = new String(readData(socket), Message.CHARSET_NAME);

        } catch (Exception e) {
            ClientExceptionUtils.handle(e, false);
        } finally {
            /** 及时断开连接 */
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                ClientExceptionUtils.handle(e);
            }
        }
        /** 服务端没有回应 */
        if ("".equals(response) || response == null) {
            listener.onError(new AIXBaseException(ExceptionCodeEnum.SERVER_NOT_RESPONSE));
            return;
        }

        Message resMsg = JsonUtils.parseObject(response, Message.class);

        if (Intent.UPLOAD_SUCCESS.match(resMsg)) {
            listener.onSuccess();
        }
    }
}
