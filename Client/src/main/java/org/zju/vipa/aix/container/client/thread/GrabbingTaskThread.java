package org.zju.vipa.aix.container.client.thread;

import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.SystemInfoUtils;
import org.zju.vipa.aix.container.common.message.SystemBriefInfo;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;

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
     * 抢任务间隔时间 15s
     */
    private static final int GRABBING_INTERVAL = 15 * 1000;

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

    public synchronized static boolean isRunning(){
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
                    TcpClient.getInstance().grabTask(info);

                    /** 休眠 */
                    try {
                        Thread.sleep(GRABBING_INTERVAL);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        ClientExceptionUtils.handle(e);
                    }
                }
            }
        };
    }


}
