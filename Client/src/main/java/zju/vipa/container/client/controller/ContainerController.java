package zju.vipa.container.client.controller;

import zju.vipa.container.network.NetworkConfig;
import zju.vipa.container.utils.ExceptionUtils;
import zju.vipa.container.network.TcpClient;
import zju.vipa.container.client.shell.ShellTask;
import zju.vipa.container.utils.FileUtils;
import zju.vipa.container.utils.LogUtils;

/**
 * @Date: 2020/1/11 20:30
 * @Author: EricMa
 * @Description: 容器控制中心
 */
public class ContainerController {
    /**
     * 守护进程休眠时间 3000s
     */
    private static final int DEAMON_SLEEP_INTERVAL = 3000 * 1000;
    /**
     * deamon检查tcp重连时间 60s
     */
    private static final int DEAMON_RECONNECTION_INTERVAL = 60 * 1000;
    private TcpClient mTcpClient;

    private String envFilePath;
    private String condarcPath = "/root/.condarc";


    private static class ControlCenterHolder {
        private static final ContainerController INSTANCE = new ContainerController();
    }

    private ContainerController() {
        mTcpClient = TcpClient.getInstance();
    }

    public static ContainerController getInstance() {
        return ControlCenterHolder.INSTANCE;
    }

    /**
     * 测试任务,
     * 1.获取yml路径
     * 2.安装conda环境,安装过程回显至服务器
     * 3.报告安装失败,请求服务器发送自定义命令
     * 4.上传手动安装过程,获得新的自定义命令
     * 5.直至训练代码执行成功,上传训练进度
     * 6.报告训练代码执行完毕,清理环境
     *
     * @param:
     * @return:
     */
    public void initTest() {

//        new ShellTask("source /root/miniconda3/bin/activate pip-env" ).exec();

        /** 更新conda源 */
        setCondaSource();


        /** 获取yml路径 */
//            getCondaYmlPath();
        /** 安装conda环境,安装过程回显至服务器 */
//        new ShellTask("conda env create -f " + envFilePath).exec();


        /** 获取pip配置路径 */
        getPipEnvFilePath();

        new ShellTask("conda create -n pip-env python=3.5 -y").exec();


//        new ShellTask("conda install pip -y").exec();
//        new ShellTask("source /root/miniconda3/bin/activate base && conda install pip -y").exec();
//
//        new ShellTask( "pip install -r " + envFilePath).exec();

        String[] task={
            "conda env list",
            "source /root/miniconda3/bin/activate pip-env",
            "conda env list",
            "conda install pip -y",
            "pip install -r " + envFilePath
        };
        new ShellTask(task).exec();




//        new ShellTask("conda env list").exec();
//        new ShellTask("conda env list && source /root/miniconda3/bin/activate pip-env && conda env list","/root/aix/code").exec();
//        new ShellTask("source /root/aix/code/pip-env.sh","/root/aix/code").exec();

//        new ShellTask("source /root/miniconda3/bin/activate pip-env").exec();

//        new ShellTask("conda env list").exec();
//        new ShellTask("source /root/miniconda3/bin/activate pip-env").exec();
//        new ShellTask("conda env list").exec();





//        new ShellTask("source /root/miniconda3/bin/activate pip-env && pip install -r " + envFilePath).exec();

//            new ShellTask("cd root/aix/code").exec();
//            new ShellTask("apt-get update").exec();


        while (true) {

            //TODO
            /** 休眠 */
            try {
                Thread.sleep(DEAMON_SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                ExceptionUtils.handle(e);
            }
        }
    }

    /**
     * 获取conda环境文件url  todo:添加下载功能
     *
     * @param:
     * @return:
     */
    private void getCondaYmlPath() {
        envFilePath = mTcpClient.getCondaEnvFileByTaskId("taskId");
        LogUtils.debug("yml file path = " + envFilePath);
//            path="/root/tmp/tf-gpu.yml";
//            path="/nfs2/mc/docker/aix-container/train_client.yml";
    }


    /**
     * 获取pip环境文件url  todo:添加下载功能
     *
     * @param:
     * @return:
     */
    private void getPipEnvFilePath() {
        envFilePath = mTcpClient.getPipEnvFileByTaskId("taskId");
        LogUtils.debug("pip file path = " + envFilePath);
//            path="/root/tmp/tf-gpu.yml";
//            path="/nfs2/mc/docker/aix-container/train_client.yml";
    }

    /**
     * 设置新的conda国内源
     *
     * @param:
     * @return:
     */
    private void setCondaSource() {
        String src = NetworkConfig.DEFAULT_CONDA_SOURCE;
        src = mTcpClient.getCondaSource();

//        new ShellTask("tee /root/.condarc << EOF\n\n"+src+"\n\nEOF").exec();
        FileUtils.write(src, condarcPath);
    }


    /**
     * 守护进程deamonTask
     * Client可随时主动发起连接
     * 主动建立连接后,若10min内没有任何数据传输,则断开连接,
     * 之后每隔10分钟尝试发起一次tcp连接,由服务器决定是否建立连接
     *
     * @param:
     * @return:
     */
    public void deamonTask() {
        while (true) {
            //todo
//            if (!mTcpClient.isSocketAvailable()){
//
//            }
            mTcpClient.deamonQuery();

            /** 休眠 */
            try {
                Thread.sleep(DEAMON_RECONNECTION_INTERVAL);
            } catch (InterruptedException e) {
                ExceptionUtils.handle(e);
            }
        }
    }
}
