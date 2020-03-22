package zju.vipa.aix.container.client.thread;

import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.message.SystemBriefInfo;
import zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import zju.vipa.aix.container.utils.LogUtils;
import zju.vipa.aix.container.utils.SystemInfoUtils;

/**
 * @Date: 2020/3/10 13:47
 * @Author: EricMa
 * @Description: 心跳线程, 以daemon模式运行：
 * 1.未建立tcp连接时（工作线程空闲），每30s询问服务器有无连接需求，同时汇报容器状态（cpu，gpu，内存占用率等等）
 * 2.已建立tcp连接时（工作线程忙碌），每一次Task执行完毕时，打包上传Task执行信息
 */
public class HeartbeatThread extends Thread {
    /**
     * deamon心跳汇报间隔时间 30s
     */
    private static final int HEARTBEAT_REPORT_INTERVAL = 30 * 1000;

    public static volatile boolean keepRunning = true;

    //    private static class HeartbeatThreadHolder{
//        private static final HeartbeatThread INSTANCE=new HeartbeatThread();
//    }
    public static HeartbeatThread getInstance() {
//        return HeartbeatThreadHolder.INSTANCE;
        return new HeartbeatThread();
    }

    private HeartbeatThread() {
        //todo daemon不行
//        this.setDaemon(true);
    }

    @Override
    public void run() {

        keepRunning = true;
        LogUtils.info("Start heartbeats report.");
        while (keepRunning) {
            //todo
//            if (!mTcpClient.isSocketAvailable()){
//            }
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
}
