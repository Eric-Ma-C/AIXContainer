package org.zju.vipa.aix.container.client.network;

/**
 * @Date: 2020/5/6 18:01
 * @Author: EricMa
 * @Description: todo:
 */
public interface UploadDataListener {
    /**
     * 更新进度
     */
    void onProgress(int progress);

    //        void onError(String msg);
    void onFinished();
}
