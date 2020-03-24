package zju.vipa.aix.container.client;

import zju.vipa.aix.container.client.task.TaskController;
import zju.vipa.aix.container.client.utils.ClientExceptionUtils;

/**
 * @Date: 2020/1/11 14:57
 * @Author: EricMa
 * @Description: 主入口
 */
public class Client {
    public static void main(String[] args) {

        ClientExceptionUtils.setDefaultUncaughtExceptionHandler();
        TaskController.getInstance().start();
//        SystemInfoUtils.getGpuInfo();
    }
}
