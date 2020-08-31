package org.zju.vipa.aix.container.common.env;

import java.util.Random;

/**
 * @Date: 2020/8/31 14:55
 * @Author: EricMa
 * @Description: conda源
 */
public enum CondaSource {

    /**
     * 清华源
     */
    TUNA ("channels:\n" +
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
        "  simpleitk: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud");

    private String config;
    private  static CondaSource[] condaSources =CondaSource.values();
    private static int condaSourceId = 0;

   CondaSource(String config) {
        this.config = config;
    }

    public static String defaultConfig() {
        return condaSources[0].config;
    }

    public static String currentConfig() {
        return condaSources[condaSourceId].config;
    }

    public static String nextConfig() {
        condaSourceId += new Random().nextInt(condaSources.length-1);
        condaSourceId %= condaSources.length;
        return currentConfig();
    }
}
