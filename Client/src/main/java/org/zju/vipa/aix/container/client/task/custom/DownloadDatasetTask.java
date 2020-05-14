package org.zju.vipa.aix.container.client.task.custom;

import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.task.BaseTask;
import org.zju.vipa.aix.container.client.utils.HttpDownloadUtils;
import org.zju.vipa.aix.container.config.Config;
import org.zju.vipa.aix.container.config.NetworkConfig;

/**
 * @Date: 2020/5/1 21:30
 * @Author: EricMa
 * @Description: 下载数据集
 */
public class DownloadDatasetTask extends BaseTask {
    String datasetId;
    private int[] delay=new int[]{3000,10000,20000,30000,60000};
    private int retry=0;

    public DownloadDatasetTask(String datasetId) {
        this.datasetId = datasetId;
    }

    @Override
    protected void run() {
        download();

        //todo 成功的反馈  下载失败的处理
//        /** 权限*/
//        new ShellTask("chmod -R 777 /home/aix/dataset").exec(shellErrorListener);
    }

    private void download(){
        HttpDownloadUtils.download(NetworkConfig.DOWNLOAD_SERVER_IP, NetworkConfig.DOWNLOAD_SERVER_PORT,
            "http://" + NetworkConfig.DOWNLOAD_SERVER_IP + ":" + NetworkConfig.DOWNLOAD_SERVER_PORT
                + "/dataset/" + datasetId + "/deviceSnapshot", Config.DATASET_SAVE_PATH, new HttpDownloadUtils.DownloadListener() {
                @Override
                public void onError(int errorCode) {
                    if (retry<5) {
                        try {
                            Thread.sleep(delay[retry++]);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        ClientLogUtils.info("正在尝试第{}次下载...",1+retry);
                        download();
                    }
                }
            });
    }
}