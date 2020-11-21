package org.zju.vipa.aix.container.common.config;

/**
 * @Date: 2020/11/21 18:01
 * @Author: EricMa
 * @Description: 各种源
 */
@Deprecated
public class Sources {
    /**
     * conda源地址
     */
    public static final String CONDA_SOURCE_TUNA = "channels:\n" +
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
