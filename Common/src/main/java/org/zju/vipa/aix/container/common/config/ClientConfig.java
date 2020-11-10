package org.zju.vipa.aix.container.common.config;

/**
 * @Date: 2020/7/1 15:05
 * @Author: EricMa
 * @Description: 训练容器配置
 */
public class ClientConfig {

    /**
     * 前十次抢任务间隔时间 ms
     */
    public static final int INITIAL_GRABBING_INTERVAL = 5000;
    /**
     * 一个周期内抢任务失败的最大次数，值越大对服务器压力越小
     */
    public static final int MAX_GRAB_TASK_FAILED_TIME = 30;

    /**
     * 容器心跳包间隔时间 15s  需不需要自动调整？
     */
    public static final int HEARTBEATS_INTERVAL_MS = 15 * 1000;
}
