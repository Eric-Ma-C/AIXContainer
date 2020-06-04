package org.zju.vipa.aix.container.client.network;

/**
 * @Date: 2020/5/6 18:01
 * @Author: EricMa
 * @Description:
 */
public interface UploadDataListener {
    /**
     * 更新进度
     */
    void onProgress(int progress);

    /**
     * 失败
     */
    void onError(Throwable cause);

    /**
     * 成功
     */
    void onSuccess();
}
