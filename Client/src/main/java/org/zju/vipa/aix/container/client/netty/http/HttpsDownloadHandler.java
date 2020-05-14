package org.zju.vipa.aix.container.client.netty.http;

import org.zju.vipa.aix.container.client.utils.HttpDownloadUtils;

/**
 * @Date: 2020/4/30 15:29
 * @Author: EricMa
 * @Description: https下载
 */
public class HttpsDownloadHandler extends HttpDownloadHandler {
    public HttpsDownloadHandler(String savePath, HttpDownloadUtils.DownloadListener listener) {
        super(savePath,listener);
    }

}
