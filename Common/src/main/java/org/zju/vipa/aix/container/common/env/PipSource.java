package org.zju.vipa.aix.container.common.env;

import java.util.Random;

/**
 * @Date: 2020/8/31 14:56
 * @Author: EricMa
 * @Description: pip源
 */
public enum PipSource {

    /**
     * 阿里云
     */
    ALIYUN("https://mirrors.aliyun.com/pypi/simple/"),
    /**
     * 中国科技大学
     */
    USTC("https://pypi.mirrors.ustc.edu.cn/simple/"),
    /**
     * 豆瓣(douban)
     */
    DOUBAN("https://pypi.douban.com/simple/"),
    /**
     * 清华大学
     */
    TUNA("https://pypi.tuna.tsinghua.edu.cn/simple/");

    private String url;
    private  static PipSource[] pipSources =PipSource.values();
    private static int pipSourceId = 0;

    PipSource(String url) {
        this.url = url;
    }

    public static String defaultUrl() {
        return pipSources[0].url;
    }

    public static String currentUrl() {
        return pipSources[pipSourceId].url;
    }

    public static String nextUrl() {
        pipSourceId += new Random().nextInt(pipSources.length-1);
        pipSourceId %= pipSources.length;
        return currentUrl();
    }
}
