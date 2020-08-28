package org.zju.vipa.aix.container.client.thread;

import org.zju.vipa.aix.container.client.Client;
import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.SystemInfoUtils;
import org.zju.vipa.aix.container.common.config.ClientConfig;
import org.zju.vipa.aix.container.common.message.SystemBriefInfo;

import java.util.Random;

/**
 * @Date: 2020/6/17 13:47
 * @Author: EricMa
 * @Description: 抢任务线程：
 * 1.未抢到任务时（工作线程空闲），每30s询问服务器有无任务可抢，同时汇报容器状态（cpu，gpu，内存占用率等等）
 * todo 2.已抢到任务时（工作线程忙碌），每一次Task执行完毕时，打包上传Task执行信息
 */
public class GrabbingTaskThread {

    private static Runnable runnable;
    /**
     * 初始抢任务间隔时间 5s
     */
    private static final int INITIAL_GRABBING_INTERVAL = 5000;
    private static int grabbingInterval = INITIAL_GRABBING_INTERVAL;

    private static volatile boolean exit = true;

    synchronized static Runnable getRunnable() {
        return runnable;
    }

    /**
     * 停止抢任务
     * 本方法不会立刻停止抢任务，需要等待本周期任务结束，线程才会终止
     */
    public synchronized static void stop() {
        exit = true;
        ClientThreadManager.getInstance().cancelUploadLog();
    }

    public synchronized static boolean isRunning() {
        return !exit;
    }

    static {
        runnable = new Runnable() {
            @Override
            public void run() {
                exit = false;
                ClientLogUtils.info("Start grabbing task.");
                while (!exit) {

                    SystemBriefInfo info = SystemInfoUtils.getSystemBriefInfo();

                    /** 若抢到了任务，会在新线程执行任务 */
                    boolean ok = TcpClient.getInstance().grabTask(info);
                    if (!ok) {/** 没抢到 */
                        if (++Client.grabTaskFailedCount > 10) {
                            /** 减慢抢任务频率 */
                            grabbingInterval += new Random().nextInt(1000);
                        }
                        if (Client.grabTaskFailedCount > ClientConfig.MAX_GRAB_TASK_INTERVAL_SECONDS) {
                            /** 复位 */
                            Client.grabTaskFailedCount = 0;
                            grabbingInterval = INITIAL_GRABBING_INTERVAL;
                        }
                        ClientLogUtils.info("暂时没有抢到任务,准备第{}次尝试,请耐心等待...", Client.grabTaskFailedCount);

                    }

                    /** 休眠 */
                    try {
                        Thread.sleep(grabbingInterval);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        ClientExceptionUtils.handle(e);
                    }
                }
            }
        };
    }


}
