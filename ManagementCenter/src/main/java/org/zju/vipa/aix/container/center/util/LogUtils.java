package org.zju.vipa.aix.container.center.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Date: 2020/2/4 20:06
 * @Author: EricMa
 * @Description: 平台日志打印,stdout输出
 */
public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

//    private static final LogLevelEnum LOG_LEVEL=LogLevelEnum.INFO;

//    /** 容器token显示 */
//    private static String getPrefix(String token){
//        return token.substring(token.length()-9)+": ";
//    }



    public static void debug(String msg,Object... objs){
        logger.debug(msg,objs);
    }





    public static void info(String msg,Object... objs){
        logger.info(msg,objs);
    }
//    public static void info(String token,String msg){
//        info("Realtime log from {}:{}",token,msg);
//    }




//    public static void worning(Message msg,Object... objs){
//        worning(msg.getToken(),msg.getValue());
//    }
public static void worning(String msg,Object... objs){
    logger.warn(msg,objs);
}






//    public static void error(Message msg){
//        error(msg.getValue()+"{}",msg.getToken(),);
//    }
    public static void error(String msg,Object... objs){
        logger.error(msg,objs);
    }





    /** @deprecated 日志等级 */
    private enum LogLevelEnum{
        DEBUG,INFO,WORNING,ERROR;

        public boolean match(LogLevelEnum logLevel){
            return this.ordinal()<=logLevel.ordinal();
        }
    }
}
