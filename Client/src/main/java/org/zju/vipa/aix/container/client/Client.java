package org.zju.vipa.aix.container.client;

import org.zju.vipa.aix.container.client.task.ClientTaskController;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.TokenUtils;

/**
 * @Date: 2020/1/11 14:57
 * @Author: EricMa
 * @Description: 主入口
 */
public class Client {
    public static final String TOKEN = TokenUtils.getDeviceToken();
    /**
     * 是否上传
     * 1.容器shell执行输出 2.容器日志
     * 若不上传，暂存在本地log文件中，需要时上传平台
     */
    public static boolean isUploadRealtimeLog = false;

    public static void main(String[] args) {

        ClientExceptionUtils.setDefaultUncaughtExceptionHandler();
        ClientTaskController.getInstance().start();
//        SystemInfoUtils.getGpuInfo();
    }
}
