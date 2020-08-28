package org.zju.vipa.aix.container.common.config;

/**
 * @Date: 2020/7/1 15:05
 * @Author: EricMa
 * @Description: 训练容器配置
 */
public class ClientConfig {
    /** 抢任务最大时间间隔,推荐300 即5min*/
    public static final int MAX_GRAB_TASK_INTERVAL_SECONDS=30;

    /**
     *  容器心跳包间隔时间 15s  todo 自动调整
     */
    public static final int HEARTBEATS_INTERVAL = 15 * 1000;
}
