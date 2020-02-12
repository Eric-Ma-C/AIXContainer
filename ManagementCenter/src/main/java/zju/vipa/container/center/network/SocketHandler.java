package zju.vipa.container.center.network;

import sun.security.ssl.Debug;
import zju.vipa.container.message.Intent;
import zju.vipa.container.message.Message;
import zju.vipa.container.utils.DebugUtils;
import zju.vipa.container.utils.ExceptionUtils;
import zju.vipa.container.utils.JsonUtils;
import zju.vipa.container.utils.LogUtils;

import java.io.*;
import java.net.Socket;

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
            LogUtils.worning("Requests is null!");
            return;
        }
        switch (msg.getIntent()) {
            case getCondaEnvFileByTaskId:
                getCondaEnvFileByTaskId(msg.getValue());
                break;
            case deamonQuery:
                deamonQuery();
                break;
            case shellInfo:
                shellInfo(msg.getValue());
                break;
            case shellResult:
                shellResult(msg.getValue());
                break;
            case shellError:
                shellError(msg.getValue());
                break;
            default:
                break;
        }


        /** 关闭socket,短连接 */
        disconnect();
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

    private void deamonQuery() {
        //todo reconnect
    }

    private void shellInfo(String value) {
//        LogUtils.debug("Shell info from " + mSocket.getInetAddress() + " :" + value);
    }

    private void shellResult(String value) {
//        LogUtils.debug("Shell result from " + mSocket.getInetAddress() + " :" + value);


    }

    private void shellError(String value) {
        LogUtils.error("Shell error from " + mSocket.getInetAddress() + " :" + value);
        //todo
    }

    /**
     * 返回conda环境文件路径
     */
    private void getCondaEnvFileByTaskId(String taskId) {

        Message msg;
//        msg = new Message(Intent.condaEnvFileUrl, "/nfs2/mc/docker/aix-container/train_client.yml");

            msg = new Message(Intent.condaEnvFileUrl, "/root/aix/code/train_client.yml");

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
        LogUtils.info(mSocket.getInetAddress() + ":" + response);

        Message msg = JsonUtils.parseObject(response.toString(), Message.class);
        return msg;
    }
}



