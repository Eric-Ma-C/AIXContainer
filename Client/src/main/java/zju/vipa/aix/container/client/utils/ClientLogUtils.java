package zju.vipa.aix.container.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zju.vipa.aix.container.client.network.ClientMessage;
import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.utils.LogUtils;

/**
 * @Date: 2020/3/27 17:37
 * @Author: EricMa
 * @Description: client专属的LogUtils
 * 定时上传本地日志文件
 * 重要日志实时上传至平台
 */
public class ClientLogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    private static final boolean UPLOAD_CLIENT_LOG=true;


    private static String getSuffix(){
//        return "\nRealtime log from "+ TokenUtils.getTokenSuffix()+": ";
        return "Realtime log: ";
    }

    public static void debug(String msg){

        logger.debug(msg);
        if (UPLOAD_CLIENT_LOG) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getSuffix() + msg));
        }

    }
    public static void debug(Message msg){
        logger.debug(msg.getValue());
        if (UPLOAD_CLIENT_LOG) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getSuffix() + msg.getValue()));
        }
    }


    public static void info(String msg){
        logger.info(msg);
        if (UPLOAD_CLIENT_LOG) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getSuffix() + msg));
        }
    }
    public static void info(Message msg){
        logger.info(msg.getValue());
        if (UPLOAD_CLIENT_LOG) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getSuffix() + msg.getValue()));
        }
    }



    public static void worning(String msg){
        logger.warn(msg);
        if (UPLOAD_CLIENT_LOG) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getSuffix() + msg));
        }
    }
    public static void worning(Message msg){
        logger.warn(msg.getValue());
        if (UPLOAD_CLIENT_LOG) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getSuffix() + msg.getValue()));
        }
    }


    public static void error(String msg){
        logger.error(msg);
        if (UPLOAD_CLIENT_LOG) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getSuffix() + msg));
        }
    }
    public static void error(Message msg){
        logger.error(msg.getValue());
        if (UPLOAD_CLIENT_LOG) {
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.REAL_TIME_LOG, getSuffix() + msg.getValue()));
        }
    }
}
