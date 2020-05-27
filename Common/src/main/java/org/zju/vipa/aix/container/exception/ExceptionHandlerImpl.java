package org.zju.vipa.aix.container.exception;

import org.zju.vipa.aix.container.config.DebugConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * @Date: 2020/5/23 15:50
 * @Author: EricMa
 * @Description: todo:
 */
public class ExceptionHandlerImpl implements ExceptionHandler{
    /**
     * 解决 e.getMessage()=null
     *
     * @param e
     * @return: java.lang.String
     */
    public String getMessage(Throwable e) {
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

    @Override
    public void handle(Throwable e) {
        /** 得到异常棧的首个元素 */
//        StackTraceElement stackTraceElement = e.getStackTrace()[0];

        if (DebugConfig.IS_LOCAL_DEBUG){
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Throwable e, String worningInfo) {

        handle(e);
    }



    /**
     * set default UncaughtExceptionHandler for main thread
     */
    public void setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                handle(e,"Exception from thread " + t.getName() + ":");
            }
        });
    }
}
