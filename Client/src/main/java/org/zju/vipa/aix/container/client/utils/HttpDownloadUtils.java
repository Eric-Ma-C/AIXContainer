package org.zju.vipa.aix.container.client.utils;

import org.zju.vipa.aix.container.client.netty.http.HttpClient;

/**
 * @Date: 2020/5/1 20:21
 * @Author: EricMa
 * @Description: http下载工具
 */
public class HttpDownloadUtils {
    public static void download(String host, int port, String url, String savePath,DownloadListener listener) {
        try {
            HttpClient.getInstance().getDownload(host, port, url, savePath,listener);
        } catch (Exception e) {
            ClientExceptionUtils.handle(e, true);
        }
    }

    public interface DownloadListener{
        void onError(int errorCode);
    }
}
