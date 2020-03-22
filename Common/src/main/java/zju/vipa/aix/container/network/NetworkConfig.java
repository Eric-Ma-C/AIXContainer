package zju.vipa.aix.container.network;

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
     * 本地测试地址
     */
    public static final String DEBUG_SERVER_IP = "127.0.0.1";
    /**
     * tcp自定义端口号
     */
    public static final int SERVER_PORT = 10007;
    /**
     * todo:socket响应数据读取超时时间5s
     */
    public static final int SOCKET_READ_TIMEOUT_DEFAULT = 5 * 1000;
    /** test container id */
    public static final String TEST_CONTAINER_ID="bfeb717242c94d60b73ad6dd695164e3";
    public static final String TEST_CONTAINER_TOKEN="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiZmViNzE3MjQyYzk0ZDYwYjczYWQ2ZGQ2OTUxNjRlMyJ9.zU16mNiGzYvuOxEq_dPI3Srv_I7rsNnH4ZUhT1TDlTY";
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
        "  simpleitk: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud\n\n";
}
