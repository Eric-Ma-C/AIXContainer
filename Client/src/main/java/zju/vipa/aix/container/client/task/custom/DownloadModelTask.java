package zju.vipa.aix.container.client.task.custom;

import zju.vipa.aix.container.client.shell.ShellTask;
import zju.vipa.aix.container.client.task.BaseTask;
import zju.vipa.aix.container.client.utils.HttpDownloadUtils;
import zju.vipa.aix.container.config.Config;
import zju.vipa.aix.container.config.NetworkConfig;

/**
 * @Date: 2020/5/1 20:23
 * @Author: EricMa
 * @Description: 下载任务
 */
public class DownloadModelTask extends BaseTask {
    String modelId;

    public DownloadModelTask(String modelId) {
        this.modelId = modelId;
    }

    @Override
    protected void run() {
        HttpDownloadUtils.download(NetworkConfig.DOWNLOAD_SERVER_IP,NetworkConfig.DOWNLOAD_SERVER_PORT,"http://"+NetworkConfig.DOWNLOAD_SERVER_IP+":"+NetworkConfig.DOWNLOAD_SERVER_PORT+"/model/"+modelId+"/file", Config.MODEL_SAVE_PATH);
        /** 解压 */
        new ShellTask("unzip -o -d "+Config.MODEL_UNZIP_PATH+" "+Config.MODEL_SAVE_PATH).exec(shellErrorListener);
    }
}
