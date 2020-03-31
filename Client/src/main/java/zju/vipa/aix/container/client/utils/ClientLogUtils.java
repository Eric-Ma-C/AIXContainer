package zju.vipa.aix.container.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zju.vipa.aix.container.client.network.ClientMessage;
import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;

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


    private static String getUploadSuffix(){
//        return "\nRealtime log from "+ TokenUtils.getTokenSuffix()+": ";
        return "Realtime log: ";
    }




    public static void debug(String msg){
        debug(msg,false);
    }
    public static void debug(Message msg,boolean isUpload){
        debug(msg.toString(),isUpload);
    }
    public static void debug(String msg,boolean isUpload){
        logger.debug(msg);
        if (isUpload) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getUploadSuffix() + msg));
        }
    }





    public static void info(String msg){
        info(msg,false);
    }
    public static void info(String msg,boolean isUpload){
        logger.info(msg);
        if (isUpload) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getUploadSuffix() + msg));
        }
    }
    public static void info(Message msg,boolean isUpload){
        info(msg.getValue(),isUpload);
    }





    public static void worning(String msg){
        worning(msg,false);
    }
    public static void worning(String msg,boolean isUpload){
        logger.warn(msg);
        if (isUpload) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getUploadSuffix() + msg));
        }
    }
    public static void worning(Message msg,boolean isUpload){
        worning(msg.getValue(),isUpload);
    }




    public static void error(String msg){
        error(msg,false);
    }
    public static void error(String msg,boolean isUpload){
        logger.error(msg);
        if (isUpload) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getUploadSuffix() + msg));
        }
    }
    public static void error(Message msg,boolean isUpload){
        error(msg.getValue(),isUpload);
    }
}
