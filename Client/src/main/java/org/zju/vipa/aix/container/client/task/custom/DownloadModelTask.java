package org.zju.vipa.aix.container.client.task.custom;

import org.zju.vipa.aix.container.client.shell.ShellTask;
import org.zju.vipa.aix.container.client.task.BaseTask;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.HttpDownloadUtils;
import org.zju.vipa.aix.container.common.config.Config;
import org.zju.vipa.aix.container.common.config.NetworkConfig;

/**
 * @Date: 2020/5/1 20:23
 * @Author: EricMa
 * @Description: 下载任务
 */
public class DownloadModelTask extends BaseTask {
    String modelId;
    private int[] delay=new int[]{3000,10000,20000,30000,60000};
    private int retry=0;

    public DownloadModelTask(String modelId) {
        this.modelId = modelId;
    }

    @Override
    protected void run() {
        download();
        /** 解压 */
        new ShellTask("unzip -o -d "+ Config.MODEL_UNZIP_PATH+" "+Config.MODEL_SAVE_PATH).exec(shellErrorListener);
    }

    private void download(){
        HttpDownloadUtils.download(NetworkConfig.DOWNLOAD_SERVER_IP, NetworkConfig.DOWNLOAD_SERVER_PORT,
            "http://" + NetworkConfig.DOWNLOAD_SERVER_IP + ":" + NetworkConfig.DOWNLOAD_SERVER_PORT +
                "/model/" + modelId + "/file", Config.MODEL_SAVE_PATH, new HttpDownloadUtils.DownloadListener() {
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
