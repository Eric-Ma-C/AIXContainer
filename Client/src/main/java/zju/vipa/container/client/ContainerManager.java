package zju.vipa.container.client;

import zju.vipa.container.client.controller.ContainerController;

/**
 * @Date: 2020/1/11 14:57
 * @Author: EricMa
 * @Description: 主入口
 */
public class ContainerManager {
    public static void main(String[] args) {
        ContainerController.getInstance().initTest();
    }
}
