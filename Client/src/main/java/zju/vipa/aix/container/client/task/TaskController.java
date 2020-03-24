package zju.vipa.aix.container.client.task;

import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.client.thread.ClientThreadManager;
import zju.vipa.aix.container.client.thread.Heartbeat;
import zju.vipa.aix.container.network.NetworkConfig;
import zju.vipa.aix.container.utils.LogUtils;
import zju.vipa.aix.container.utils.SystemInfoUtils;

import java.util.LinkedList;
import java.util.Queue;


/**
 * @Date: 2020/1/11 20:30
 * @Author: EricMa
 * @Description: 容器任务管理中心
 */
public class TaskController {
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
        private static final TaskController INSTANCE = new TaskController();
    }

    private TaskController() {
        init();
    }

    public static TaskController getInstance() {
        return TaskControllerHolder.INSTANCE;
    }

    private void init() {
        taskQueue = new LinkedList<>();
        currentTask = null;
    }

    /**
     * 启动容器心跳线程和工作线程
     *
     * @param:
     * @return:
     */
    public void start() {


        //todo 容器id
        boolean isSuccessful = TcpClient.getInstance().registerContainer(NetworkConfig.TEST_CONTAINER_TOKEN);
        if (isSuccessful) {
            TcpClient.getInstance().uploadGpuInfo(SystemInfoUtils.getGpuInfo());
            ClientThreadManager.getInstance().startHeartbeat();
//            Heartbeat.getInstance().start();
        } else {
            LogUtils.error("容器注册失败，请检查token配置。");
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
            LogUtils.worning("The adding task is null.");
            return;
        }

        taskQueue.add(task);
        execNewTask();
    }

    private void execNewTask() {
        if (taskQueue.isEmpty()) {
            LogUtils.info("Task Queue is empty.");
            /** 启动心跳线程 */
            ClientThreadManager.getInstance().startHeartbeat();
//            Heartbeat.getInstance().start();

            return;
        } else {
            if (currentTask != null && currentTask.getState() != TaskState.FINISHED) {
                LogUtils.worning("Current task has not finished.");
                return;
            } else {
                /** 保证poll()不返回null */
                currentTask = taskQueue.poll();
                currentTask.run(new BaseTask.TaskStateListener() {
                    @Override
                    public void onBegin() {
                    }

                    @Override
                    public void onFinished() {
                        execNewTask();
                    }
                });
            }
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
