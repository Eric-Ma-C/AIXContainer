package zju.vipa.aix.container.client.thread;

import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.message.SystemBriefInfo;
import zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import zju.vipa.aix.container.utils.LogUtils;
import zju.vipa.aix.container.utils.SystemInfoUtils;

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
    /** 线程池 */
//    private ExecutorService singleThreadExecutor;
    /**
     * deamon心跳汇报间隔时间 30s
     */
    private static final int HEARTBEAT_REPORT_INTERVAL = 30 * 1000;

    private static volatile boolean exit = false;

    protected static Runnable getRunnable(){
        return runnable;
    }
    /** 停止心跳线程 */
    public static void stop(){
        exit=true;
    }
    static {
        runnable =new Runnable() {
            @Override
            public void run() {
                exit = false;
                LogUtils.info("Start heartbeats report.");
                while (!exit) {

                    SystemBriefInfo info = SystemInfoUtils.getSystemBriefInfo();

                    TcpClient.getInstance().heartbeatReport(info);

                    /** 休眠 */
                    try {
                        Thread.sleep(HEARTBEAT_REPORT_INTERVAL);
                    } catch (InterruptedException e) {
                        ClientExceptionUtils.handle(e);
                    }
                }
            }
        };
    }


}
