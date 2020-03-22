package zju.vipa.aix.container.client;

import zju.vipa.aix.container.client.controller.ContainerController;
import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import zju.vipa.aix.container.utils.LogUtils;

/**
 * @Date: 2020/1/11 14:57
 * @Author: EricMa
 * @Description: 主入口
 */
public class ContainerManager {
    public static void main(String[] args) {

        ClientExceptionUtils.setDefaultUncaughtExceptionHandler();
        ContainerController.getInstance().start();
//        SystemInfoUtils.getGpuInfo();
    }
}
