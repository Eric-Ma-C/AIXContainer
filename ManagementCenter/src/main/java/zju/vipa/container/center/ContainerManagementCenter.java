package zju.vipa.container.center;

import zju.vipa.container.center.network.CenterTcpServer;
import zju.vipa.container.network.NetworkConfig;
import zju.vipa.container.utils.ExceptionUtils;

import java.io.IOException;

/**
 * @Date: 2020/1/9 20:05
 * @Author: EricMa
 * @Description: 启动入口
 */
public class ContainerManagementCenter {


        public static void main(String[] args) {
                try {
                        CenterTcpServer.start(NetworkConfig.SERVER_PORT);
                } catch (IOException e) {
                        ExceptionUtils.handle(e);
                }
        }
}
