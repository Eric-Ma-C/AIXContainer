package org.zju.vipa.aix.container.common.config;

/**
 * @Date: 2020/2/6 12:03
 * @Author: EricMa
 * @Description: 本地测试配置
 */

public class DebugConfig {
    /**
     * 使用
     */
//    public static final boolean IS_LOCAL_DEBUG = true;
    public static final boolean IS_LOCAL_DEBUG = false;

    /**
     * 是否打开Server网络消息收发日志
     * 警告：由于服务器网络IO日志量较大，仅可在测试时打开
     */
    public static final boolean SERVER_NETWORK_IO_LOG = true;
    /**
     * 是否打开client网络消息收发日志
     */
//    public static final boolean CLIENT_NETWORK_IO_LOG = true;
    public static final boolean CLIENT_NETWORK_IO_LOG = false;

    /**
     * socket响应数据读取超时时间500s
     */
    public static int SOCKET_READ_TIMEOUT_DEBUG = 500 * 1000;

    /**
     * 是否记录Netty日志信息
     */
    public static final boolean OPEN_NETTY_LOG = false;

    /**
     * 是否是下载模型模式
     * 否则为挂载路径模式,不需要下载
     */
    public static final boolean IS_DOWNLOAD_MODULE = false;


}
