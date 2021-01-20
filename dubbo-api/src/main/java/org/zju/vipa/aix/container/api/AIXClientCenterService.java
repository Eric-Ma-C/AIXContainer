package org.zju.vipa.aix.container.api;

import org.zju.vipa.aix.container.api.entity.AixDeviceVO;
import org.zju.vipa.aix.container.api.entity.RunningClient;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;
import org.zju.vipa.aix.container.common.env.KnownErrorRuntime;
import org.zju.vipa.aix.container.common.message.GpuInfo;
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
     * =有任务的容器数量+正在抢任务的容器数量
     */
    int getOnlineClientNum();

    /**
     * 有任务的容器数量
     */
    int getActiveClientNum();

    /**
     * 获取一次center实时日志,读到日志文件结尾,一般有多行
     *
     * @param
     * @return: java.lang.String
     */
    String serverLogReadToEnd();

    /**
     * 开始获取center实时日志
     *
     * @param
     * @return: void
     */
    void serverLogInit();

    /**
     * 停止获取center实时日志
     *
     * @param
     * @return: void
     */
    void serverLogStop();


    /**
     * 正在与平台通信的训练容器列表
     * 也就是待发送队列的容器列表
     */
    List<RunningClient> getClientList();

    /**
     * 容器任务日志，包括命令和错误信息
     * 抢到新任务后清空
     */
    List<String> getTaskLogsByToken(String token);

    /**
     * 获取某容器的待发送队列
     *
     * @param token 容器token
     * @return: void
     */
    List<Message> getMessageQueueByToken(String token);

    /**
     * 获取某容器的当前任务
     *
     * @param token 容器token
     * @return: void
     */
    Task getTaskByToken(String token);

    /**
     * 获取某容器的当前gpu状态
     *
     * @param token 容器token
     * @return: void
     */
    GpuInfo getGpuInfoByToken(String token);

    /**
     * 开始获取client实时日志
     *
     * @param token 容器token
     * @return: void
     */
    void clientLogInit(String token);

    /**
     * 停止获取client实时日志
     *
     * @param token 容器token
     * @return: void
     */
    void clientLogStop(String token);

    /**
     * 强行停止client正在执行的任务
     *
     * @param token 容器token
     * @return: void
     */
    void clientTaskStop(String token);

    /**
     * 返回容器信息
     *
     * @param id 容器id
     * @return: AixDeviceVO
     */
    AixDeviceVO getDeviceInfoById(String id);


    /**
     *   注册容器数量
     * @param
     * @return: int
     */
    int getClientCount();


    /**
     *  分页查询
     * @param page 查询第几页
     * @param countPerPage 每页的条数(一般固定值)
     * @return: java.util.List<org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice>
     */
    List<AixDeviceVO> getClientListByPage(int page, int countPerPage);

    /**
     *  更新容器
     * @param
     * @return: int
     */
    boolean updateDeviceNameById(String id,String newName);
    /**
     *  更新容器
     * @param
     * @return: int
     */
    boolean updateDeviceInfoById(String id,String newInfo);

    void refreshRuntimeErrorList(List<KnownErrorRuntime> knownErrorRuntimeList);
}
