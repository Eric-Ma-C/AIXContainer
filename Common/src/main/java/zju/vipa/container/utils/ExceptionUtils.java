package zju.vipa.container.utils;

import zju.vipa.container.message.Intent;
import zju.vipa.container.network.TcpClient;

/**
 * @Date: 2020/1/11 15:27
 * @Author: EricMa
 * @Description:集中异常处理
 */
public class ExceptionUtils {

    public static void handle(Exception e) {
        /** 得到异常棧的首个元素 */
        StackTraceElement stackTraceElement = e.getStackTrace()[0];

        String msg=e.getMessage();
        System.out.println(msg);

        TcpClient.getInstance().uploadState(Intent.EXCEPTION, msg);

//        System.out.println(e.getClass().getName()+" at "
//            + stackTraceElement.getClassName() + "."
//            + stackTraceElement.getMethodName() + "  "
//            + "line " + stackTraceElement.getLineNumber()
//            + "    " + TimeUtils.getTimeStr()+":\n"+
//            e.getMessage());

//        e.printStackTrace();
    }

    public static void handle(Exception e, String worningInfo) {
        LogUtils.worning(worningInfo);
        handle(e);
    }
}
