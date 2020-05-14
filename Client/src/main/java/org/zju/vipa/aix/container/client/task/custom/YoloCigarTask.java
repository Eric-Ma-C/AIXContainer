package org.zju.vipa.aix.container.client.task.custom;

import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.client.shell.ShellTask;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.task.BaseTask;

/**
 * @Date: 2020/3/10 9:39
 * @Author: EricMa
 * @Description: 抢到任务配置yolo香烟识别环境
 */
public class YoloCigarTask extends BaseTask {
    private String mlTaskId;
    private String condaEnvFilePath;

    public YoloCigarTask() {
    }

    private YoloCigarTask(String mlTaskId) {
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
     * 获取conda环境文件url
     *
     * @param:
     * @return:
     */
    private void getCondaYmlPath() {
        condaEnvFilePath = TcpClient.getInstance().getCondaEnvFileByTaskId(mlTaskId);
        ClientLogUtils.debug("yml file path = {}" , condaEnvFilePath);
            condaEnvFilePath="/home/aix/code/environment.yaml";
//            path="/nfs2/mc/docker/aix-container/train_client.yml";
    }

//    @Override
//    protected String[] initTaskCmds() {
//        String[] cmds={
////            "conda env create -f " + condaEnvFilePath,
//            "source /home/aix/miniconda3/bin/activate clean_yolo",
//            "python /nfs2/sontal/codes/TrainerProxy/main.py"
//        };
//        return cmds;
//    }

    @Override
    protected  void run() {
        /** 更新conda源 */
//        setCondaSource();
        /** 获取yml路径 */
        getCondaYmlPath();

        new ShellTask(getCommands()).exec(shellErrorListener);

    }
}
