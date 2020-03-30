package zju.vipa.aix.container.client.utils;

import zju.vipa.aix.container.client.network.ClientMessage;
import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;

import zju.vipa.aix.container.utils.ExceptionUtils;
import zju.vipa.aix.container.utils.LogUtils;

/**
 * @Date: 2020/1/11 15:27
 * @Author: EricMa
 * @Description: client中异常处理，比ExceptionUtils多了上传
 */
public class ClientExceptionUtils {


    public static void handle(Throwable e) {
        ExceptionUtils.handle(e);
        TcpClient.getInstance().sendMessage(new ClientMessage(Intent.EXCEPTION, ExceptionUtils.getMessage(e)));

    }

    public static void handle(Throwable e, String worningInfo) {
        ExceptionUtils.handle(e, worningInfo);
        TcpClient.getInstance().sendMessage(new ClientMessage(Intent.EXCEPTION, worningInfo + " " + ExceptionUtils.getMessage(e)));
    }

    /**
     * set default UncaughtExceptionHandler for main thread
     */
    public static void setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                handle(e, "Exception from thread " + t.getName() + ":");
            }
        });
    }
}
