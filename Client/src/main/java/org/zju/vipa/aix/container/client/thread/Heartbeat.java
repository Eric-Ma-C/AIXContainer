package org.zju.vipa.aix.container.client.thread;

import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.message.SystemBriefInfo;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.SystemInfoUtils;

/**
 * @Date: 2020/3/10 13:47
 * @Author: EricMa
 * @Description: 心跳线程：
 * 1.未建立tcp连接时（工作线程空闲），每30s询问服务器有无任务可抢，同时汇报容器状态（cpu，gpu，内存占用率等等）
 * 2.已建立tcp连接时（工作线程忙碌），每一次Task执行完毕时，打包上传Task执行信息
 */
public class Heartbeat {
    /**
     * 执行代码
     */
    private static Runnable runnable;
    /**
     * deamon心跳汇报间隔时间 30s
     */
    private static final int HEARTBEAT_REPORT_INTERVAL = 30 * 1000;

    private static volatile boolean exit = true;

    protected static Runnable getRunnable() {
        return runnable;
    }

    /**
     * 停止心跳线程
     * 本方法不会立刻停止心跳线程，需要等待本心跳周期任务结束，线程才会终止
     */
    public static void stop() {
        exit = true;
        ClientThreadManager.getInstance().cancelUploadLog();
    }

    public static boolean isRunning(){
        return !exit;
    }

    static {
        runnable = new Runnable() {
            @Override
            public void run() {
                exit = false;
                ClientLogUtils.info("Start heartbeats report.");
                while (!exit) {

                    SystemBriefInfo info = SystemInfoUtils.getSystemBriefInfo();

                    /** 若抢到了任务，会在新线程执行任务 */
                    TcpClient.getInstance().heartbeatReport(info);

                    /** 休眠 */
                    try {
                        Thread.sleep(HEARTBEAT_REPORT_INTERVAL);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        ClientExceptionUtils.handle(e);
                    }
                }
            }
        };
    }


}
