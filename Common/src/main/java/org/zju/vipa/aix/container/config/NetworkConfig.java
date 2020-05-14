package org.zju.vipa.aix.container.config;

import org.zju.vipa.aix.container.utils.DebugUtils;

/**
 * @Date: 2020/2/8 22:18
 * @Author: EricMa
 * @Description: 网络参数配置
 */
public class NetworkConfig {
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
     * 本地测试地址
     */
    public static final String DEBUG_SERVER_IP = "127.0.0.1";

    //    public static String SERVER_IP = MY_ALIYUN_SERVER_IP;
    public static String SERVER_IP = VIPA_205_IP;
    public static String DOWNLOAD_SERVER_IP = VIPA_205_IP;
    public static int DOWNLOAD_SERVER_PORT = 8080;

    static {
        if (DebugUtils.IS_LOCAL_DEBUG) {
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
     * tcp自定义端口号
     */
    public static final int SERVER_PORT = 10007;
    /**
     * socket响应数据读取超时时间5s
     */
    public static int SOCKET_READ_TIMEOUT_DEFAULT;

    static {
        if (DebugUtils.IS_LOCAL_DEBUG) {
            SOCKET_READ_TIMEOUT_DEFAULT = DebugUtils.SOCKET_READ_TIMEOUT_DEBUG;
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
