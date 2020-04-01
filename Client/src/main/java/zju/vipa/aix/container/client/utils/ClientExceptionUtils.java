package zju.vipa.aix.container.client.utils;

import zju.vipa.aix.container.client.network.ClientMessage;
import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.message.Intent;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;


/**
 * @Date: 2020/1/11 15:27
 * @Author: EricMa
 * @Description: client中异常处理，记录log，默认上传至平台
 */
public class ClientExceptionUtils {

    public static void handle(Throwable e,boolean isUpload) {
        String msg = getMessage(e);
        /** 网络错误导致的Exception不再上传 */
        if (isUpload) {
            printLogAndUpload(msg);
        }
    }
    public static void handle(Throwable e) {
        /** 默认上传 */
        handle(e,true);
    }

    public static void handle(String worningInfo,Throwable e,boolean isUpload) {
        String msg = worningInfo+"  "+getMessage(e);
        if (isUpload) {
            printLogAndUpload(msg);
        }
    }
    public static void handle(String worningInfo,Throwable e) {
        handle(worningInfo,e,true);
    }

    private static void printLogAndUpload(String msg){
        ClientLogUtils.error(msg,true);
        TcpClient.getInstance().sendMessage(new ClientMessage(Intent.EXCEPTION, msg));
    }

    /**
     * 解决 e.getMessage()=null
     *
     * @param e
     * @return: java.lang.String
     */
    private static String getMessage(Throwable e) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        e.printStackTrace(ps);
        String msg = e.getMessage();
        try {
            msg = os.toString("UTF8");
        } catch (UnsupportedEncodingException ex) {
            handle(ex);
        }
        return msg;
    }


    /**
     * set default UncaughtExceptionHandler for main thread
     */
    public static void setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                handle("Exception from thread " + t.getName() + ":",e );
            }
        });
    }
}
