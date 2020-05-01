package zju.vipa.aix.container.client.task.custom;

import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.client.shell.ShellTask;
import zju.vipa.aix.container.client.task.BaseTask;
import zju.vipa.aix.container.client.utils.ClientLogUtils;

/**
 * @Date: 2020/3/10 11:05
 * @Author: EricMa
 * @Description: 抢到任务在conda中配置pip环境
 */
public class PipConfigTask extends BaseTask {
    private String mlTaskId;
    private String pipEnvFilePath;

    public PipConfigTask() {
    }

    private PipConfigTask(String mlTaskId) {
        super();//可以省略
        this.mlTaskId = mlTaskId;
    }
    /**
     * 设置新的conda国内源
     * @param:
     * @return:
     */
    private void setCondaSource() {
        String src = TcpClient.getInstance().getCondaSource();

        new ShellTask("tee /home/aix/.condarc << EOF\n" + src + "\nEOF").exec();
//        FileUtils.write(src, condarcPath);
    }

    /**
     * 获取pip环境文件url
     *
     * @param:
     * @return:
     */
    private void getPipEnvFilePath() {
        pipEnvFilePath = TcpClient.getInstance().getPipEnvFileByTaskId(mlTaskId);
        ClientLogUtils.debug("pip file path = {}" , pipEnvFilePath);
//            path="/root/tmp/tf-gpu.yml";
//            path="/nfs2/mc/docker/aix-container/train_client.yml";
    }

//    @Override
//    protected String[] initTaskCmds() {
//        String[] cmds={
//            "conda create -n pip-env python=3.6 -y",
//            "conda env list",
//            "source /root/miniconda3/bin/activate pip-env",
//            "conda env list",
//            "conda install pip -y",
//            "pip install -r " + pipEnvFilePath};
//
//        return cmds;
//    }

    @Override
    protected void run() {
        /** 更新conda源 */
        setCondaSource();

        /** 获取pip配置路径 */
        getPipEnvFilePath();



        new ShellTask(getCommands()).exec(shellErrorListener);
    }


}
