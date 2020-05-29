package org.zju.vipa.aix.container.center.log;

import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.config.Config;
import org.zju.vipa.aix.container.config.DebugConfig;
import org.zju.vipa.aix.container.exception.AIXBaseException;
import org.zju.vipa.aix.container.exception.ExceptionCodeEnum;

import java.io.File;
import java.io.IOException;

/**
 * @Date: 2020/3/31 14:05
 * @Author: EricMa
 * @Description: 分容器存储日志
 */
public class ClientLogFileManager {

    public static final String CLIENT_LOG_PATH="/log/aixlog/";
    public static final String CLIENT_LOG_NAME="debug.log4j.2020-04-20";


    private static class ClientLogFileManagerHolder {
        private static final ClientLogFileManager INSTANCE=new ClientLogFileManager();
    }
    private ClientLogFileManager(){
        if (ClientLogFileManagerHolder.INSTANCE!=null){
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }
    }

    public static ClientLogFileManager getInstence(){
        return ClientLogFileManagerHolder.INSTANCE;
    }


    public String getFilePathByDate(String time){
        if (time==null){
            /** 当天的日志 */
            return CLIENT_LOG_PATH+"debug.log4j";
        }else {
            return CLIENT_LOG_PATH+"debug.log4j."+time;
        }
    }

    /** 平台存储路径 */
    public File getSavePath(String token,String clientPath){
        /** 存储在应用部署根目录下的client-log中 */
        String saveDir,savePath;
        if (DebugConfig.IS_LOCAL_DEBUG){
            saveDir="E:\\aixTest";
            savePath=saveDir + "\\" + clientPath.substring(clientPath.lastIndexOf('\\'));
        }else {
            saveDir = Config.AIX_SERVER_ROOT_DIR+ "/client-log/" + token ;
            savePath = saveDir + "/" + clientPath.substring(clientPath.lastIndexOf('/'));
        }

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
