package org.zju.vipa.aix.container.center.network;

import org.zju.vipa.aix.container.message.Message;

/**
 * @Date: 2020/5/7 11:45
 * @Author: EricMa
 * @Description: 定义服务端通信接口
 */
public interface ServerIO {
    /**
     * 发送响应数据
     *
     * @param msg
     * @param isClose 是否断开tcp连接
     *                有些时候（如上传文件）会多次交换信息（Message）
     *                才会断开连接，以提高网络利用率
     *
     * @return: void
     */
    void response(Message msg, boolean isClose);

    /**
     * 发送响应数据,并断开tcp连接
     *
     * @param msg
     * @return: void
     */
    void response(Message msg);


//    Message readRequests();

    /**
     * 保存上传的文件
     *
     * @param msg 待上传文件的信息
     * @return: void
     */
    void saveData(Message msg);

}
