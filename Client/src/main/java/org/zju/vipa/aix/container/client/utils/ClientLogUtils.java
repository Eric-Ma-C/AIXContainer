package org.zju.vipa.aix.container.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zju.vipa.aix.container.client.Client;
import org.zju.vipa.aix.container.client.network.ClientMessage;
import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.common.message.Intent;

/**
 * @Date: 2020/3/27 17:37
 * @Author: EricMa
 * @Description: client专属的LogUtils
 * 定时上传本地日志文件
 * 重要日志实时上传至平台
 */
public class ClientLogUtils {
    private static final Logger logger = LoggerFactory.getLogger(ClientLogUtils.class);

//    private static boolean isUpload = true;

    /**
     * 上传平台显示前缀
     */
    @Deprecated
    private static String getUploadSuffix() {
        return "Realtime log: ";
    }


    public static void debug(String msg, Object... objs) {
        debug(Client.isUploadRealtimeLog, msg, objs);
    }

    //    public static void debug(Message msg,boolean isUpload){
//        debug(msg.toString(),isUpload);
//    }
    public static void debug(boolean isUpload, String msg, Object... objs) {
        logger.debug(msg, objs);
        if (isUpload) {
            upload(msg, objs);
        }
    }


    public static void info(String msg, Object... objs) {
        info(Client.isUploadRealtimeLog, msg, objs);
    }

    //    public static void info(boolean isUpload,Message msg,Object... objs){
//        info(isUpload,msg.getValue(),objs);
//    }
    public static void info(boolean isUpload, String msg, Object... objs) {
        logger.info(msg, objs);
        if (isUpload) {
            upload(msg, objs);
        }
    }


    public static void worning(String msg, Object... objs) {
        worning(Client.isUploadRealtimeLog, msg, objs);
    }

    //    public static void worning(Message msg,boolean isUpload){
//        worning(msg.getValue(),isUpload);
//    }
    public static void worning(boolean isUpload, String msg, Object... objs) {
        logger.warn(msg, objs);
        if (isUpload) {
            upload(msg, objs);
        }
    }


    public static void error(String msg, Object... objs) {
        error(Client.isUploadRealtimeLog, msg, objs);
    }

    public static void error(boolean isUpload, String msg, Object... objs) {
        logger.error(msg, objs);
        if (isUpload) {
            upload(msg, objs);
        }
    }

    private static void upload(String msg, Object... objs) {
        TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, String.format(msg.replaceAll("\\{}", "%s"), objs)));
//        TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, msg));
    }
}
