package org.zju.vipa.aix.container.client.network;

import org.zju.vipa.aix.container.message.Message;

/**
 * @Date 2020/5/12 17:20
 * @Author EricMa
 * @Description 定义大妈机通信接口
 */
public interface ClientIO {
    /**
     * 通过tcp连接,发送msg,获取响应信息
     * 短连接方式
     *
     * @param message 待发送消息
     * @return zju.vipa.aix.container.message.Message 响应消息
     */
    Message sendMsgAndGetResponse(ClientMessage message);

    /**
     * 通过tcp连接,发送msg,获取响应信息
     * 短连接方式
     *
     * @param message 待发送消息
     * @param readTimeOut      超时时间
     * @return 响应消息
     */
    Message sendMsgAndGetResponse(ClientMessage message, int readTimeOut);

    /**
     * 发送消息
     * @param message
     * @return: void
     */
    void sendMessage(ClientMessage message);

    /**
     *  上传（日志）
     * @param path 待上传文件路径
     * @param listener 回调
     * @return NIO异步操作，结果在回调返回
     */
    void upLoadData(String path, UploadDataListener listener);
}
