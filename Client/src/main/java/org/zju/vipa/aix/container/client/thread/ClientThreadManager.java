package org.zju.vipa.aix.container.client.thread;

import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.UploadUtils;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Date: 2020/3/23 10:05
 * @Author: EricMa
 * @Description: 容器线程管理器，线程池实现
 */
public class ClientThreadManager {
    /**
     * 上次上传日志的时间
     */
    private long lastUploadTime = 0;
    ScheduledExecutorService uploadLogService;

    private static class ClientThreadManagerHolder {
        private static final ClientThreadManager INSTANCE = new ClientThreadManager();
    }

    private ClientThreadManager() {
        if (ClientThreadManagerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }
        initThreadPool();
    }

    public static ClientThreadManager getInstance() {
        return ClientThreadManagerHolder.INSTANCE;
    }

    /**
     * 线程池
     */
    private static ThreadPoolExecutor mThreadPoolExecutor;

    /**
     * 执行新任务
     */
    public void startNewTask(Runnable runnable) {
        ClientLogUtils.debug("ClientThreadManager.startNewTask() runnable={}", runnable);
        mThreadPoolExecutor.execute(runnable);
    }

    /**
     * 开始抢任务
     */
    public void startGrabbingTask() {

//        ClientLogUtils.debug("startGrabbingTask");
        if (!GrabbingTaskThread.isRunning()) {
            lastUploadTime = System.currentTimeMillis();

            /** todo 延时上传日志，若停止抢任务（已经在训练模型）会暂停本任务 */
            if (System.currentTimeMillis() - lastUploadTime > 3600 * 24 * 1000) {
                uploadLogFilePerDay();
            }

            mThreadPoolExecutor.execute(GrabbingTaskThread.getRunnable());
        } else {
            ClientLogUtils.worning("Grabbing Task Thread is Already Running!");
        }

    }

    /**
     * 初始化,开始心跳,抢任务
     */
    public void init() {

        mThreadPoolExecutor.execute(new HeartBeatThread());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ClientExceptionUtils.handle(e);
        }
        startGrabbingTask();

    }

    /**
     * 空闲时上传前一天的日志
     */
    private void uploadLogFilePerDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        final File file = new File("/log/aixlog/debug.log4j." + yesterday);
        if (file.exists()) {
            uploadLogService = Executors.newScheduledThreadPool(1);
            //延时30*60秒执行
            uploadLogService.schedule(new Runnable() {
                @Override
                public void run() {

                    /** 停止抢任务线程,执行日志上传 */
                    GrabbingTaskThread.stop();
                    UploadUtils.uploadFile(file.getPath());
                }
            }, 30 * 60, TimeUnit.SECONDS);
        }
    }


    public void cancelUploadLog() {
        if (uploadLogService != null && !uploadLogService.isShutdown()) {
            uploadLogService.shutdown();
        }
    }

    /**
     * 初始化线程池，注意根据容器硬件参数修改线程池参数
     *
     * @return: void
     */
    private static void initThreadPool() {
        /** 核心线程池大小 */
        int corePoolSize = 5;
        /** 最大线程池大小 */
        int maximumPoolSize = 30;
        /** 超过corePoolSize后,线程最大空闲时间,超出后将被销毁 */
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        /** 超过maximumPoolSize后的线程等待队列 */
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(20);
        /** 线程创建工厂 */
        ThreadFactory threadFactory = new ClientTreadFactory();
        /** 超出workQueue容量后的拒绝策略 */
        RejectedExecutionHandler handler = new ClientIgnorePolicy();

        mThreadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize,
            keepAliveTime, unit,
            workQueue, threadFactory, handler);

        // 预启动所有核心线程
        mThreadPoolExecutor.prestartAllCoreThreads();
    }

    /**
     * 可以更改线程的名称，线程组，优先级，守护进程状态等
     */
    private static class ClientTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "t" + mThreadNum.getAndIncrement());
            ClientLogUtils.info("{} has been created", t.getName());
            return t;
        }
    }

    /**
     * 拒绝策略
     */
    private static class ClientIgnorePolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            /** 直接拒绝不执行新任务 */
            doLog(r, e);
        }

        /**
         * 可做日志记录等
         */
        private void doLog(Runnable r, ThreadPoolExecutor e) {
            //
            ClientLogUtils.error("{} rejected from {}", r.toString(), e.toString());
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }
}
