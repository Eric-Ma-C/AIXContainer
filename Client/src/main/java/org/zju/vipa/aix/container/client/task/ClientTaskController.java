package org.zju.vipa.aix.container.client.task;

import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.client.task.custom.ClientShellTask;
import org.zju.vipa.aix.container.client.thread.ClientThreadManager;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.TokenUtils;
import org.zju.vipa.aix.container.client.utils.UploadUtils;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.utils.TimeUtils;

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


    private static class ClientTaskControllerHolder {
        private static final ClientTaskController INSTANCE = new ClientTaskController();
    }

    private ClientTaskController() {
        if (ClientTaskControllerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }
        init();
    }

    public static ClientTaskController getInstance() {
        return ClientTaskControllerHolder.INSTANCE;
    }

    private void init() {
        taskQueue = new ConcurrentLinkedQueue<>();
        currentTask = null;
    }

    /**
     * 启动容器抢任务线程和工作线程
     *
     * @param:
     * @return:
     */
    public void start() {

        /** 上传验证容器token */
        boolean isSuccessful = TcpClient.getInstance().registerContainer(TokenUtils.getDeviceToken());
        /** 此处判断可以减少非法token的容器发送大量无效抢任务请求 */
        if (isSuccessful) {
            /** 验证成功，开始心跳,抢任务线程 */
            ClientThreadManager.getInstance().init();

            /** 测试上传 */
            if (DebugConfig.IS_LOCAL_DEBUG) {
//                UploadUtils.uploadFile("E:\\tobedelete.txt");
                UploadUtils.uploadFile("E:\\Redis-x64-3.2.100.zip");

            } else {
                //            UploadUtils.uploadFile("/log/aixlog/debug.log4j");
//                UploadUtils.uploadFile("/home/aix/Client.jar");
            }

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

        taskQueue.add(task);
        ClientLogUtils.debug("Task added:{}", task);

        execNewTask();
    }

    /**
     * client容器执行新任务
     */
    private synchronized void execNewTask() {

        boolean noTaskRunning = (currentTask == null || TaskState.FINISHED.match(currentTask.getState()));

        if (!noTaskRunning) {
            ClientLogUtils.info("Current task has not finished.Wait for execution.");
            return;

        } else if (noTaskRunning && taskQueue.isEmpty()) {

            /** 向平台请求任务 */
            ClientLogUtils.info("Client Task Queue is empty.Ask for new work.");
            TcpClient.getInstance().askForCmds();

            return;

        } else {/** noTaskRunning && !taskQueue.isEmpty() */

            /** 给currentTask赋新值，保证poll()不返回null */
            currentTask = taskQueue.poll();
            ClientLogUtils.debug("ClientTaskController.execNewTask:{}", currentTask);

            /** 在新线程执行新任务 */
            ClientThreadManager.getInstance().startNewTask(currentTask.getRunnable(new BaseTask.TaskStateListener() {
                @Override
                public void onBegin() {
                    ClientLogUtils.info("\n\n\n----- Task begin -----:\n{}\n", currentTask);

                }

                @Override
                public void onFinished() {
                    ClientLogUtils.info("\n-----  Task finished in {}  -----:\n{}\n\n\n", TimeUtils.getInterval(currentTask.getExecTime()), currentTask);
                    String repairCmds = currentTask.getRepairCmds();
                    if (repairCmds != null) {
                        ClientLogUtils.debug("currentTask.getRepairCmds()={}", repairCmds);
                        /** 修复运行环境 */
                        BaseTask task = new ClientShellTask(repairCmds);
                        task.setTaskInfo(currentTask.getCodePath(), currentTask.getModelArgs());
                        addTask(task);
                    } else {
                        execNewTask();
                    }


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
//                            Thread.currentThread().interrupt();
//            }
//        }
//    }


}
