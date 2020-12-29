package org.zju.vipa.aix.container.center.log;

import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.common.utils.PropertyUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * @Date: 2020/6/3 15:17
 * @Author: EricMa
 * @Description:
 */
public class ServerLogReader {
    //    private static volatile boolean stop = false;
    private static RandomAccessFile raf = null;

    public static void stop() {
        if (raf != null) {
            try {
                raf.close();
            } catch (IOException e) {
                ExceptionUtils.handle(e);
            }
        }
    }

    public static void readInit() {
//        stop = false;

        String filePath=PropertyUtils.getProperty("log4j.properties","log4j.appender.fileLog.File");
        try {
            raf = new RandomAccessFile(filePath, "r");
//            raf = new RandomAccessFile("/nfs2/mc/docker/aix-center/log/123", "r");
            //指定末尾的位置

            long fp = raf.length() > 1000 ? raf.length() - 1000 : 0;

            raf.seek(fp);

        } catch (IOException e) {
            ExceptionUtils.handle(e);
        }

    }

    public static String readToEnd() {
//        readInit();

        if (raf==null){
            return null;
        }
        String str=null;
        try {
//            LogUtils.debug("raf.length()={}",raf.length());

            int newLogLen= (int) (raf.length()-raf.getFilePointer());
            if (newLogLen>1000){
                newLogLen=1000;
            }
            LogUtils.debug("newLogLen={}",newLogLen);
            byte[] bytes=new byte[newLogLen];
            raf.read(bytes);
            str = new String(bytes, Charset.forName("UTF-8"));
//            LogUtils.error("str={}",str);

        } catch (IOException e) {
            ExceptionUtils.handle(e);
        }
        return str;
    }





    private static void randomAccess(String path, String mode, long seek, int skipBytes) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, mode);
            raf.seek(seek);//指定记录指针的位置
            //System.out.println(raf.readLine());//使用seek指针指向0，readLine读取所有内容
            raf.getFilePointer();//获取指针的位置
            raf.skipBytes(skipBytes);//跳过的字节数

            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = raf.read(buff)) > 0) {
                System.out.println(new String(buff, 0, len));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
