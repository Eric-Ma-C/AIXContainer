package zju.vipa.container.utils;

/**
 * @Date: 2020/2/4 20:06
 * @Author: EricMa
 * @Description: 日志打印,stdout输出
 */
public class LogUtils {


    private static final LogLevelEnum LOG_LEVEL=LogLevelEnum.DEBUG;

    public static void info(String msg){
        if (LOG_LEVEL.match(LogLevelEnum.INFO)){
            System.out.println("INFO:"+msg);
        }
    }

    public static void worning(String msg){
        if (LOG_LEVEL.match(LogLevelEnum.WORNING)){
            System.out.println("WORNING:"+msg);
        }
    }

    public static void debug(String msg){
        if (LOG_LEVEL.match(LogLevelEnum.DEBUG)){
            System.out.println("DEBUG:"+msg);
        }
    }
    public static void error(String msg){
        if (LOG_LEVEL.match(LogLevelEnum.ERROR)){
            System.out.println("ERROR:"+msg);
        }
    }


    /** 日志等级 */
    private enum LogLevelEnum{
        INFO,WORNING,DEBUG,ERROR;

        public boolean match(LogLevelEnum logLevel){
            return this.ordinal()>=logLevel.ordinal();
        }
    }
}
