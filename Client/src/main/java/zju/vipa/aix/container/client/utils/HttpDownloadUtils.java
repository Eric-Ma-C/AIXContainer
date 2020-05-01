package zju.vipa.aix.container.client.utils;

import zju.vipa.aix.container.client.netty.HttpClient;

/**
 * @Date: 2020/5/1 20:21
 * @Author: EricMa
 * @Description: http下载工具
 */
public class HttpDownloadUtils {
    public static void download(String host, int port, String url, String savePath) {
        try {
            HttpClient.getInstance().getDownload(host, port, url, savePath);
        } catch (Exception e) {

            ClientExceptionUtils.handle(e, true);

        }

    }
}
