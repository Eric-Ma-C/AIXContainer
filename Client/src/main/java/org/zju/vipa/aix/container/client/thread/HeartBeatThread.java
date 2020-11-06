package org.zju.vipa.aix.container.client.thread;

import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.SystemInfoUtils;
import org.zju.vipa.aix.container.common.config.ClientConfig;

/**
 * @Date: 2020/6/18 19:48
 * @Author: EricMa
 * @Description: 心跳线程
 * 定时汇报容器状态
 * 抢到任务后仍然运行
 * 平台长时间未收到心跳则被判定为容器离线,下次连接需要重新向平台注册
 */
public class HeartBeatThread implements Runnable{



//    private static boolean isRunning=false;


    @Override
    public void run() {
        Thread.currentThread().setName("heartbeat");
        ClientLogUtils.info("Start heartbeat.");
        while (true) {

//                    SystemBriefInfo info = SystemInfoUtils.getSystemBriefInfo();

            TcpClient.getInstance().heartbeatReport(SystemInfoUtils.getGpuInfo());

            /** 休眠 */
            try {
                Thread.sleep(ClientConfig.HEARTBEATS_INTERVAL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ClientExceptionUtils.handle(e);
            }
        }
    }
}
