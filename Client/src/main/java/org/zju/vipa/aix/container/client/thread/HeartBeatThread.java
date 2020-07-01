package org.zju.vipa.aix.container.client.thread;

import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.SystemInfoUtils;

/**
 * @Date: 2020/6/18 19:48
 * @Author: EricMa
 * @Description: 心跳线程
 * 定时汇报容器状态
 * 平台长时间未收到心跳则被判定为容器离线,下次连接需要重新向平台注册
 */
public class HeartBeatThread implements Runnable{

    /**
     * todo 心跳间隔时间 30s  自动调整?
     */
    private static final int HEARTBEATS_INTERVAL = 15 * 1000;

//    private static boolean isRunning=false;


    @Override
    public void run() {
        ClientLogUtils.info("Start heart beat.");
        while (true) {

//                    SystemBriefInfo info = SystemInfoUtils.getSystemBriefInfo();

            TcpClient.getInstance().heartbeatReport(SystemInfoUtils.getGpuInfo());

            /** 休眠 */
            try {
                Thread.sleep(HEARTBEATS_INTERVAL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ClientExceptionUtils.handle(e);
            }
        }
    }
}
