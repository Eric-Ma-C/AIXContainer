package zju.vipa.aix.container.client.utils;

import zju.vipa.aix.container.client.Client;
import zju.vipa.aix.container.client.network.TcpClient;

import java.io.*;

/**
 * @Date: 2020/4/23 13:22
 * @Author: EricMa
 * @Description: 容器文件上传
 */
public class UploadUtils {

    public static boolean uploadFile(final String path){
        //首先询问服务器是否接受文件，忙的时候可能会拒绝

        TcpClient.getInstance().requestUpload(path, new TcpClient.UploadDataListener() {
            @Override
            public void onProgress(int progress) {
                ClientLogUtils.info("File uploaded {}%",progress);
            }

            @Override
            public void onFinished() {
                ClientLogUtils.info("File upload successful!");
            }
        });



//
//        ClientLogUtils.debug("SEND:\n{}\n" , msg);
//
//        InputStream stream=null;
//        try {
//             stream=new FileInputStream(path);
//        } catch (FileNotFoundException e) {
//            ClientLogUtils.info(true,"Upload file {} is not exists.",path);
//            return false;
//        }
//        // 封装文件
//        BufferedInputStream bis = new BufferedInputStream(stream);
//
//        TcpClient.getInstance().upLoadData(bis,);
        return true;
    }

}
