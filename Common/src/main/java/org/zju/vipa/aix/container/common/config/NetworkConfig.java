package org.zju.vipa.aix.container.common.config;

/**
 * @Date: 2020/2/8 22:18
 * @Author: EricMa
 * @Description: 网络参数配置
 */
public class NetworkConfig {
    /**
     * server是否使用netty作为网络通信框架
     * 否则使用socket阻塞IO
     */
    public static  boolean SERVER_USE_NETTY = true;
    /**
     * client是否使用netty作为网络通信框架
     * 否则使用socket阻塞IO
     */
    public static  boolean CLIENT_USE_NETTY = true;
    /**
     * my aliyun
     */
    public static final String MY_ALIYUN_SERVER_IP = "112.124.46.179";
    /**
     * vipa aliyun
     */
    public static final String VIPA_ALIYUN_SERVER_IP = "120.55.242.132";
    /**
     * vipa 内网205
     */
    public static final String VIPA_205_IP = "10.214.211.205";
    /**
     * vipa 内网206
     */
    public static final String VIPA_206_IP = "10.214.211.206";
    /**
     * vipa 内网207
     */
    public static final String VIPA_207_IP = "10.214.211.207";
    /**
     * 本地测试地址
     */
    public static final String DEBUG_SERVER_IP = "127.0.0.1";

    /**
     * redis服务器地址
     */
    public static final String REDIS_SERVER_IP = "127.0.0.1";
    /**
     * redis服务器端口
     */
    public static final int REDIS_SERVER_PORT = 6379;

    /**
     * zk服务器地址
     */
    public static final String ZK_SERVER_URL = "zookeeper://112.124.46.179:2181";
    /**
     * zk服务端口
     */
    public static final int ZK_AIX_PORT = 20880;
    /**
     * kafka broker服务器地址
     */
    public static final String KAFKA_SERVER_URL = "112.124.46.179:9092";
    public static final String KAFKA_GROUP_ID = "aix-group";
    public static final String KAFKA_TOPIC="client-realtime-log";

    //    public static String SERVER_IP = MY_ALIYUN_SERVER_IP;
    public static String SERVER_IP = VIPA_207_IP;
    public static String DOWNLOAD_SERVER_IP = VIPA_207_IP;
    //    public static String DOWNLOAD_SERVER_IP = MY_ALIYUN_SERVER_IP;
    public static int DOWNLOAD_SERVER_PORT = 8080;

    static {
        if (DebugConfig.IS_LOCAL_DEBUG) {
            SERVER_IP = DEBUG_SERVER_IP;
        }
    }
//    public static String getServerIp() {
//        if (DebugUtils.IS_LOCAL_DEBUG) {
//            SERVER_IP = DEBUG_SERVER_IP;
//        }
//        return SERVER_IP;
//    }

    /**
     * 消息交换 tcp自定义端口号
     */
    public static final int SERVER_PORT_MESSAGE = 10007;
    /**
     * 文件上传 tcp自定义端口号
     */
    public static final int SERVER_PORT_FILE_UPLOAD = 10008;
    /**
     * socket响应数据读取超时时间5s
     */
    public static int SOCKET_READ_TIMEOUT_DEFAULT;

    static {
        if (DebugConfig.IS_LOCAL_DEBUG) {
            SOCKET_READ_TIMEOUT_DEFAULT = DebugConfig.SOCKET_READ_TIMEOUT_DEBUG;
        } else {
            SOCKET_READ_TIMEOUT_DEFAULT = 5 * 1000;
        }
    }

    /**
     * test container id
     */
    public static final String TEST_CONTAINER_ID = "bfeb717242c94d60b73ad6dd695164e3";
    public static final String TEST_CONTAINER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiZmViNzE3MjQyYzk0ZDYwYjczYWQ2ZGQ2OTUxNjRlMyJ9.zU16mNiGzYvuOxEq_dPI3Srv_I7rsNnH4ZUhT1TDlTY";

    public static final String CENTER_ID = "aix-center";
    public static final String CENTER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhaXgtY2VudGVyIn0.-l8OW-Z9ebMS7TJA1rYUt4xFk8Bu8pG2T6qssV_77NU";


    /**
     * conda源地址
     */
    public static final String DEFAULT_CONDA_SOURCE = "channels:\n" +
        "  - defaults\n" +
        "show_channel_urls: true\n" +
        "channel_alias: https://mirrors.tuna.tsinghua.edu.cn/anaconda\n" +
        "default_channels:\n" +
        "  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main\n" +
        "  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free\n" +
        "  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/r\n" +
        "  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/pro\n" +
        "  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/msys2\n" +
        "custom_channels:\n" +
        "  conda-forge: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud\n" +
        "  msys2: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud\n" +
        "  bioconda: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud\n" +
        "  menpo: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud\n" +
        "  pytorch: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud\n" +
        "  simpleitk: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud";
}
