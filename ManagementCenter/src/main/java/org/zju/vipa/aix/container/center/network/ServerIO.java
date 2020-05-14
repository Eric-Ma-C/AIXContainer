package org.zju.vipa.aix.container.center.network;

import org.zju.vipa.aix.container.message.Message;

/**
 * @Date: 2020/5/7 11:45
 * @Author: EricMa
 * @Description: 定义服务端通信接口
 */
public interface ServerIO {
    void response(Message msg);

//    Message readRequests();

    void saveData(Message msg);
}
