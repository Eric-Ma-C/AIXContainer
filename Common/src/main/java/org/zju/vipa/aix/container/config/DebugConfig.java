package org.zju.vipa.aix.container.config;

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
     * socket响应数据读取超时时间500s
     */
    public static int SOCKET_READ_TIMEOUT_DEBUG = 500 * 1000;

    /**
     * 是否记录Netty日志信息
     */
    public static final boolean OPEN_NETTY_LOG = true;

    /**
     * 是否是下载模型模式
     * 否则为挂载路径模式,不需要下载
     */
    public static final boolean IS_DOWNLOAD_MODULE = false;


}
