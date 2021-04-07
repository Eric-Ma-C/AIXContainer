package org.zju.vipa.aix.container.common.env;

import org.zju.vipa.aix.container.common.db.entity.aix.Source;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Date: 2020/8/31 14:56
 * @Author: EricMa
 * @Description: pip源
 */
public class PipSource {

//
//    /**
//     * 阿里云
//     */
//    ALIYUN("https://mirrors.aliyun.com/pypi/simple/"),
//    /**
//     * 中国科技大学
//     */
//    USTC("https://pypi.mirrors.ustc.edu.cn/simple/"),
//    /**
//     * 豆瓣(douban)
//     */
//    DOUBAN("https://pypi.douban.com/simple/"),
//    /**
//     * 清华大学
//     */
//    TUNA("https://pypi.tuna.tsinghua.edu.cn/simple/");

    private static CopyOnWriteArrayList<String> pipSources=new CopyOnWriteArrayList<>();

    //    private String url;
//    private  static PipSource[] pipSources =PipSource.values();
    private static int pipSourceId = 0;

//    PipSource(String url) {
//        this.url = url;
//    }

    public static void refreshSource(List<Source> sources) {
        pipSources.clear();
        for (Source s : sources) {
            if ("PIP".equals(s.getType())) {
                pipSources.add(s.getUrl());
            }
        }
    }

    public static String defaultUrl() {
        return pipSources.get(0);
    }

    public static String currentUrl() {
        return pipSources.get(pipSourceId);
    }

    public static String nextUrl() {
        pipSourceId += (new Random().nextInt(pipSources.size() - 2) + 1);
        pipSourceId %= pipSources.size();
        return currentUrl();
    }
}
