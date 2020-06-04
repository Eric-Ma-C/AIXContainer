package org.zju.vipa.aix.container.center.network;

import org.zju.vipa.aix.container.center.log.ClientLogFileManager;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.utils.ByteUtils;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @Date: 2020/5/7 11:48
 * @Author: EricMa
 * @Description: socketIO 实现类
 */
public class SocketIoImpl implements ServerIO {
    /**
     * socket对象
     */
    private Socket mSocket;

    public SocketIoImpl(Socket mSocket) {
        this.mSocket = mSocket;
    }

    /**
     * 读取发送过来的tcp请求报文
     *
     * @param:
     * @return: 可能为null
     */
    public Message readRequests() {
        String request = null;

        try {
            request = new String(readData(), Message.CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            ExceptionUtils.handle(e);
        }

        Message msg = JsonUtils.parseObject(request, Message.class);

        if (msg == null) {
            disconnect();
            return null;
        }
        if (DebugConfig.SERVER_NET_IO_LOG) {
            /** 收到客户端日志 */
            LogUtils.debug("RECEIVE MSG FROM {} TOKEN: {}:\n{}\n", mSocket.getInetAddress(), msg.getTokenSuffix(), msg);
        }
        return msg;
    }

    /**
     * 写返回消息
     *
     * @param: msg
     * @return:
     */
    @Override
    public void response(Message msg,boolean isClose) {
        if (msg == null) {
            LogUtils.worning("Response messags is null!");
            return;
        }

        if (DebugConfig.SERVER_NET_IO_LOG) {
            LogUtils.debug("RESPONSE TO {}:\n{}\n", mSocket.getInetAddress(), msg);
        }

        try {
            sendData(JsonUtils.toJSONString(msg).getBytes(Message.CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            ExceptionUtils.handle(e);
        } finally {
            /** 返回报文发送后，关闭连接 */
            if (isClose) {
                disconnect();
            }
        }

    }
    /**
     * 写返回消息，并关闭连接
     *
     * @param: msg
     * @return:
     */
    @Override
    public void response(Message msg) {
        response(msg,true);
    }


    /**
     * 保存数据
     */
    @Override
    public void saveData(Message msg) {

        File saveFile = ClientLogFileManager.getInstence().getSavePath(msg.getToken(), msg.getValue());

        try {
            // 封装通道内流
            BufferedInputStream inputStream = new BufferedInputStream(mSocket.getInputStream());
            // 封装文件
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));

            byte[] bytes = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
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
     * 返回数据
     */
    private void sendData(byte[] data) {
        OutputStream outputStream = null;
        try {
            outputStream = mSocket.getOutputStream();
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
    private byte[] readData() {

        byte[] reciveBytes = new byte[0];
        InputStream inputStream = null;
        try {
            inputStream = mSocket.getInputStream();
            ByteBuffer buffer = ByteBuffer.allocate(4);

            /** 阻塞方法 */
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
}
