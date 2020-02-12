package zju.vipa.container.client.controller;

import zju.vipa.container.utils.ExceptionUtils;
import zju.vipa.container.client.network.TcpClient;
import zju.vipa.container.client.shell.ShellTask;
import zju.vipa.container.utils.LogUtils;

/**
 * @Date: 2020/1/11 20:30
 * @Author: EricMa
 * @Description: 容器控制中心 单例
 */
public class ContainerController {
    /**
     * 最短tcp周期任务时间 300s
     */
    private static final int MINIMUM_TCP_CONNECTION_INTERVAL = 300 * 1000;
    /**
     * deamon检查tcp重连时间 60s
     */
    private static final int DEAMON_RECONNECTION_INTERVAL = 60 * 1000;
    private TcpClient mTcpClient;

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
        while (true) {

            /** 1.获取yml路径 */
            String path = "";
            try {
                path = mTcpClient.getCondaEnvFileByTaskId("taskId");
            } catch (Exception e) {
                ExceptionUtils.handle(e);
            }
            LogUtils.debug("yml file path = " + path);
//            path="/root/tmp/tf-gpu.yml";
//            path="/nfs2/mc/docker/aix-container/train_client.yml";

            /** 2.安装conda环境,安装过程回显至服务器 */
//            new ShellTask("cd root/aix/code").exec();
//            new ShellTask("apt-get update").exec();



            new ShellTask("conda env create -f " + path).exec();










            /** 休眠 */
            try {
                Thread.sleep(MINIMUM_TCP_CONNECTION_INTERVAL);
            } catch (InterruptedException e) {
                ExceptionUtils.handle(e);
            }
        }
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
