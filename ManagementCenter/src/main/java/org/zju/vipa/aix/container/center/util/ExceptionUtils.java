package org.zju.vipa.aix.container.center.util;

import org.zju.vipa.aix.container.config.DebugConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * @Date: 2020/1/11 15:27
 * @Author: EricMa
 * @Description: 集中异常处理
 */
public class ExceptionUtils {

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

    public static void handle(Throwable e) {
        /** 得到异常棧的首个元素 */
//        StackTraceElement stackTraceElement = e.getStackTrace()[0];

        String msg = getMessage(e);
        LogUtils.error(msg);

        if (DebugConfig.IS_LOCAL_DEBUG){
           e.printStackTrace();
        }
    }

    public static void handle(Throwable e, String worningInfo) {
        LogUtils.worning(worningInfo);
        handle(e);
    }



    /**
     * set default UncaughtExceptionHandler for main thread
     */
    public static void setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                handle(e,"Exception from thread " + t.getName() + ":");
            }
        });
    }
}
