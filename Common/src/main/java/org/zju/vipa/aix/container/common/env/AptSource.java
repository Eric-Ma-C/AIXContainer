package org.zju.vipa.aix.container.common.env;

import java.util.Random;

/**
 * @Date: 2020/11/21 18:11
 * @Author: EricMa
 * @Description: apt
 */
public enum AptSource {
    /**
     * 阿里云
     */
    ALIYUN("deb http://mirrors.aliyun.com/ubuntu/ xenial main\n" +
        "deb-src http://mirrors.aliyun.com/ubuntu/ xenial main\n" +
        "\n" +
        "deb http://mirrors.aliyun.com/ubuntu/ xenial-updates main\n" +
        "deb-src http://mirrors.aliyun.com/ubuntu/ xenial-updates main\n" +
        "\n" +
        "deb http://mirrors.aliyun.com/ubuntu/ xenial universe\n" +
        "deb-src http://mirrors.aliyun.com/ubuntu/ xenial universe\n" +
        "deb http://mirrors.aliyun.com/ubuntu/ xenial-updates universe\n" +
        "deb-src http://mirrors.aliyun.com/ubuntu/ xenial-updates universe\n" +
        "\n" +
        "deb http://mirrors.aliyun.com/ubuntu/ xenial-security main\n" +
        "deb-src http://mirrors.aliyun.com/ubuntu/ xenial-security main\n" +
        "deb http://mirrors.aliyun.com/ubuntu/ xenial-security universe\n" +
        "deb-src http://mirrors.aliyun.com/ubuntu/ xenial-security universe\n"),
    /**
     * 中国科技大学
     */
    USTC("# 默认注释了源码仓库，如有需要可自行取消注释\n" +
        "deb https://mirrors.ustc.edu.cn/ubuntu/ xenial main restricted universe multiverse\n" +
        "# deb-src https://mirrors.ustc.edu.cn/ubuntu/ xenial main restricted universe multiverse\n" +
        "deb https://mirrors.ustc.edu.cn/ubuntu/ xenial-updates main restricted universe multiverse\n" +
        "# deb-src https://mirrors.ustc.edu.cn/ubuntu/ xenial-updates main restricted universe multiverse\n" +
        "deb https://mirrors.ustc.edu.cn/ubuntu/ xenial-backports main restricted universe multiverse\n" +
        "# deb-src https://mirrors.ustc.edu.cn/ubuntu/ xenial-backports main restricted universe multiverse\n" +
        "deb https://mirrors.ustc.edu.cn/ubuntu/ xenial-security main restricted universe multiverse\n" +
        "# deb-src https://mirrors.ustc.edu.cn/ubuntu/ xenial-security main restricted universe multiverse"),
    /**
     * 腾讯云软件源
     */
    TENCENT("deb http://mirrors.cloud.tencent.com/ubuntu/ xenial main restricted universe multiverse\n" +
        "deb http://mirrors.cloud.tencent.com/ubuntu/ xenial-security main restricted universe multiverse\n" +
        "deb http://mirrors.cloud.tencent.com/ubuntu/ xenial-updates main restricted universe multiverse\n" +
        "#deb http://mirrors.cloud.tencent.com/ubuntu/ xenial-proposed main restricted universe multiverse\n" +
        "#deb http://mirrors.cloud.tencent.com/ubuntu/ xenial-backports main restricted universe multiverse\n" +
        "deb-src http://mirrors.cloud.tencent.com/ubuntu/ xenial main restricted universe multiverse\n" +
        "deb-src http://mirrors.cloud.tencent.com/ubuntu/ xenial-security main restricted universe multiverse\n" +
        "deb-src http://mirrors.cloud.tencent.com/ubuntu/ xenial-updates main restricted universe multiverse\n" +
        "#deb-src http://mirrors.cloud.tencent.com/ubuntu/ xenial-proposed main restricted universe multiverse\n" +
        "#deb-src http://mirrors.cloud.tencent.com/ubuntu/ xenial-backports main restricted universe multiverse"),
    /**
     * 清华大学
     */
    TUNA("deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial main restricted universe multiverse\n" +
        "# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial main restricted universe multiverse\n" +
        "deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-updates main restricted universe multiverse\n" +
        "# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-updates main restricted universe multiverse\n" +
        "deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-backports main restricted universe multiverse\n" +
        "# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-backports main restricted universe multiverse\n" +
        "deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-security main restricted universe multiverse\n" +
        "# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-security main restricted universe multiverse");

    private String url;
    private  static AptSource[] aptSources = AptSource.values();
    private static int aptSourceId = 0;

    AptSource(String url) {
        this.url = url;
    }

    public static String defaultUrl() {
        return aptSources[0].url;
    }

    public static String currentUrl() {
        return aptSources[aptSourceId].url;
    }

    public static String nextUrl() {
        aptSourceId += (new Random().nextInt(aptSources.length-2)+1);
        aptSourceId %= aptSources.length;
        return currentUrl();
    }
}