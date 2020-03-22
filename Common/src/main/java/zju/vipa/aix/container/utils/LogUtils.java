package zju.vipa.aix.container.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Date: 2020/2/4 20:06
 * @Author: EricMa
 * @Description: 日志打印,stdout输出
 */
public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    private static final LogLevelEnum LOG_LEVEL=LogLevelEnum.INFO;

    public static void debug(String msg){
        logger.debug(msg);

//        if (LOG_LEVEL.match(LogLevelEnum.DEBUG)){
//            System.out.println("DEBUG:"+msg);
//        }
    }

    public static void info(String msg){
        logger.info(msg);
//        if (LOG_LEVEL.match(LogLevelEnum.INFO)){
//            System.out.println("INFO:"+msg);
//        }
    }

    public static void worning(String msg){
        logger.warn(msg);

//        if (LOG_LEVEL.match(LogLevelEnum.WORNING)){
//            System.out.println("WORNING:"+msg);
//        }
    }


    public static void error(String msg){
        logger.error(msg);

//        if (LOG_LEVEL.match(LogLevelEnum.ERROR)){
//            System.out.println("ERROR:"+msg);
//        }
    }


    /** @deprecated 日志等级 */
    private enum LogLevelEnum{
        DEBUG,INFO,WORNING,ERROR;

        public boolean match(LogLevelEnum logLevel){
            return this.ordinal()<=logLevel.ordinal();
        }
    }
}
