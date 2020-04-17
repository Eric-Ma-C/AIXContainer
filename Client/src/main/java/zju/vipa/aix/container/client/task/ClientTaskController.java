package zju.vipa.aix.container.client.task;

import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.client.task.custom.ClientShellTask;
import zju.vipa.aix.container.client.thread.ClientThreadManager;
import zju.vipa.aix.container.client.utils.ClientLogUtils;
import zju.vipa.aix.container.client.utils.TokenUtils;
import zju.vipa.aix.container.client.utils.SystemInfoUtils;
import zju.vipa.aix.container.utils.TimeUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * @Date: 2020/1/11 20:30
 * @Author: EricMa
 * @Description: 容器任务管理中心
 */
public class ClientTaskController {
    /**
     * 待执行任务队列
     */
    private Queue<BaseTask> taskQueue;

    /**
     * 当前执行的task
     */
    private BaseTask currentTask;

//
//    private String envFilePath;
//    private String condarcPath = "/root/.condarc";


    private static class TaskControllerHolder {
        private static final ClientTaskController INSTANCE = new ClientTaskController();
    }

    private ClientTaskController() {
        init();
    }

    public static ClientTaskController getInstance() {
        return TaskControllerHolder.INSTANCE;
    }

    private void init() {
        taskQueue = new ConcurrentLinkedQueue<>();
        currentTask = null;
    }

    /**
     * 启动容器心跳线程和工作线程
     *
     * @param:
     * @return:
     */
    public void start() {

        /** 上传验证容器token */
        boolean isSuccessful = TcpClient.getInstance().registerContainer(TokenUtils.getDeviceToken());
        /** 此处判断可以减少非法token的容器发送大量无效心跳请求 */
        if (isSuccessful) {
            /** 验证成功，上传实时gpu信息，开始心跳线程 */
            TcpClient.getInstance().uploadGpuInfo(SystemInfoUtils.getGpuInfo());
            ClientThreadManager.getInstance().startHeartbeat();

        } else {
            ClientLogUtils.error("容器注册失败，请检查token配置。");
        }
    }

    /**
     * 将新任务加入执行队列，准备执行
     *
     * @param:
     * @return:
     */
    public void addTask(BaseTask task) {
        if (task == null) {
            ClientLogUtils.worning("The adding task is null.");
            return;
        }
        ClientLogUtils.debug("Task added:" + task);
        taskQueue.add(task);
        execNewTask();
    }

    /**
     * client容器执行新任务
     */
    private synchronized void execNewTask() {

        boolean noTaskRunning = (currentTask == null || currentTask.getState() == TaskState.FINISHED);

        if (!noTaskRunning) {
            ClientLogUtils.error("Current task has not finished.");
            return;

        } else if (noTaskRunning && taskQueue.isEmpty()) {

            /** 向平台请求任务 */
            ClientLogUtils.info("Client Task Queue is empty.Ask for new work.");
            TcpClient.getInstance().askForWork();

//            ClientLogUtils.info("Task Queue is empty.Start heartbeats report.");
//            /** 启动心跳线程 */
//            ClientThreadManager.getInstance().startHeartbeat();
            return;

        } else {/** noTaskRunning && !taskQueue.isEmpty() */

            /** 给currentTask赋新值，保证poll()不返回null */
            currentTask = taskQueue.poll();
            ClientLogUtils.debug("ClientTaskController.execNewTask:" + currentTask.toString());

            /** 在新线程执行新任务 */
            ClientThreadManager.getInstance().startNewTask(currentTask.getRunnable(new BaseTask.TaskStateListener() {
                @Override
                public void onBegin() {
//                    ClientLogUtils.debug("****** Task begin ******:" + currentTask.toString());
                }

                @Override
                public void onFinished() {
                    ClientLogUtils.debug("----  Task finished in "+ TimeUtils.getInterval(currentTask.getExecTime()) +" ----:" + currentTask.toString(),true);
                    if (currentTask.getRepairCmds()!=null){
                        /** 修复运行环境 */
                        ClientShellTask task = new ClientShellTask(currentTask.getRepairCmds());
                        addTask(task);
                    }

                    execNewTask();
                }
            }));

        }
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
//    public void initTest() {


    /** 安装conda环境,安装过程回显至服务器 */
//        new ShellTask("conda env create -f " + envFilePath).exec();


//        new ShellTask("conda create -n pip-env python=3.6 -y").exec();


//        new ShellTask("conda install pip -y").exec();
//        new ShellTask("source /root/miniconda3/bin/activate base && conda install pip -y").exec();
//
//        new ShellTask( "pip install -r " + envFilePath).exec();

//        String[] task = {
//            "conda env list",
//            "source /root/miniconda3/bin/activate pip-env",
//            "conda env list",
//            "conda install pip -y",
//            "pip install -r " + envFilePath
//        };
//        new ShellTask(task).exec();


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


//        while (true) {
//
//
//            /** 休眠 */
//            try {
//                Thread.sleep(DEAMON_SLEEP_INTERVAL);
//            } catch (InterruptedException e) {
//                ExceptionUtils.handle(e);
//            }
//        }
//    }


}
