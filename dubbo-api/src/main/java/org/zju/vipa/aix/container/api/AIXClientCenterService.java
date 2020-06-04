package org.zju.vipa.aix.container.api;

import org.zju.vipa.aix.container.common.entity.Task;
import org.zju.vipa.aix.container.common.message.Message;

import java.util.List;

/**
 * @Date: 2020/5/31 10:38
 * @Author: EricMa
 * @Description: rpc接口
 */
public interface AIXClientCenterService {
    /**
     * 获取在线客户端数量
     */
    int getOnlineNum();

    /**
     *  获取一次实时日志,读到日志文件结尾,一般有多行
     * @param
     * @return: java.lang.String
     */
    String srverLogReadToEnd();

    /**
     *   开始获取实时日志
     * @param
     * @return: void
     */
    void srverLogInit();

    /**
     *   停止获取实时日志
     * @param
     * @return: void
     */
    void srverLogStop();


    /**
     * 正在与平台通信的训练容器数量
     * 也就是待发送队列的数量
     */
     int getActiveClientNum() ;

    /**
     * 获取某容器的待发送队列
     */
     List<Message> getMessageQueueByToken(String token) ;

    /**
     * 获取某容器的当前任务
     */
     Task getTaskByToken(String token);


}
