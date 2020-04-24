package zju.vipa.aix.container.center.log;

import org.apache.log4j.DailyRollingFileAppender;
import zju.vipa.aix.container.center.util.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @Date: 2020/3/31 14:05
 * @Author: EricMa
 * @Description: 分容器存储日志
 */
public class ClientLogManager {

    public static final String CLIENT_LOG_PATH="/log/aixlog/";
    public static final String CLIENT_LOG_NAME="debug.log4j.2020-04-20";


    private static class ClientLogManagerHolder{
        private static final ClientLogManager INSTANCE=new ClientLogManager();
    }
    private ClientLogManager(){
        if (ClientLogManagerHolder.INSTANCE!=null){
            throw new RuntimeException("单例模式不可以创建多个对象");
        }
    }

    public static ClientLogManager getInstence(){
        return ClientLogManagerHolder.INSTANCE;
    }


    public String getFilePathByDate(String time){
       return CLIENT_LOG_PATH+"debug.log4j."+time;
    }

    /** 平台存储路径 */
    public File getSavePath(String token,String clientPath){
        String saveDir = "/root/aix/client-log/" + token ;
        String savePath = saveDir + "/" + clientPath.substring(clientPath.lastIndexOf('/'));

        File dir=new File(saveDir);
        if (!dir.exists()){
            dir.mkdirs();
        }

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            try {
                /** 创建新文件 */
                saveFile.createNewFile();
            } catch (IOException e) {
                ExceptionUtils.handle(e);
            }
        }

        return saveFile;
    }
}
