package zju.vipa.aix.container.center;

import zju.vipa.aix.container.center.network.CenterTcpServer;
import zju.vipa.aix.container.network.NetworkConfig;
import zju.vipa.aix.container.utils.ExceptionUtils;

import java.io.IOException;

/**
 * @Date: 2020/1/9 20:05
 * @Author: EricMa
 * @Description: aix容器管理平台 启动入口
 */
public class ContainerManagementCenter {


        public static void main(String[] args) {
                try {
                        CenterTcpServer.getInstance().start(NetworkConfig.SERVER_PORT);
                } catch (IOException e) {
                        ExceptionUtils.handle(e);
                }
        }
}
