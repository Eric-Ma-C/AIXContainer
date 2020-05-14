//package zju.vipa.aix.container.client.log;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import zju.vipa.aix.container.message.Message;
//
///**
// * @Date: 2020/2/4 20:06
// * @Author: EricMa
// * @Description: 平台日志打印,stdout输出
// */
//public class LogUtils {
//    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);
//
////    private static final LogLevelEnum LOG_LEVEL=LogLevelEnum.INFO;
//
//    private static String getPrefix(String token){
//        return "\n"+token.substring(token.length()-9)+": ";
//    }
//
//
//
//    public static void debug(String msg){
//        logger.debug(msg);
//    }
//    public static void debug(Message msg){
////        logger.debug(msg.getValue());
//        debug(msg.getToken(),msg.getValue());
//    }
//    public static void debug(String token,String msg){
//        logger.debug(getPrefix(token)+msg);
//    }
//
//
//
//
//    public static void info(String msg){
//        logger.info(msg);
//    }
//    public static void info(Message msg){
////        logger.info(msg.getValue());
//        info(msg.getToken(),msg.getValue());
//    }
//    public static void info(String token,String msg){
//        logger.info(getPrefix(token)+msg);
//    }
//
//
//    public static void worning(String msg){
//        logger.warn(msg);
//    }
//    public static void worning(Message msg){
////        logger.warn(msg.getValue());
//        worning(msg.getToken(),msg.getValue());
//    }
//    public static void worning(String token,String msg){
//        logger.warn(getPrefix(token)+msg);
//    }
//
//
//
//
//
//    public static void error(String msg){
//        logger.error(msg);
//    }
//    public static void error(Message msg){
////        logger.error(msg.getValue());
//        error(msg.getToken(),msg.getValue());
//    }
//    public static void error(String token,String msg){
//        logger.error(getPrefix(token)+msg);
//    }
//
//
//
//
//
//    /** @deprecated 日志等级 */
//    private enum LogLevelEnum{
//        DEBUG,INFO,WORNING,ERROR;
//
//        public boolean match(LogLevelEnum logLevel){
//            return this.ordinal()<=logLevel.ordinal();
//        }
//    }
//}
