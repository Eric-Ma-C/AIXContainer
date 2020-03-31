package zju.vipa.aix.container.client.thread;

import zju.vipa.aix.container.client.utils.ClientLogUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Date: 2020/3/23 10:05
 * @Author: EricMa
 * @Description: 容器线程管理器，线程池实现
 */
public class ClientThreadManager {
    private static class ClientThreadManagerHolder {
        private static final ClientThreadManager INSTANCE = new ClientThreadManager();
    }

    private ClientThreadManager() {
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
        ClientLogUtils.debug("ClientThreadManager.startNewTask() runnable="+runnable);
        mThreadPoolExecutor.execute(runnable);
    }

    /**
     * 开始心跳汇报
     */
    public void startHeartbeat() {
        mThreadPoolExecutor.execute(Heartbeat.getRunnable());
    }

    /**
     * 初始化线程池，注意根据容器硬件参数修改线程池参数
     *
     * @return: void
     */
    private static void initThreadPool() {
        /** 核心线程池大小 */
        int corePoolSize = 2;
        /** 最大线程池大小 */
        int maximumPoolSize = 4;
        /** 线程最大空闲时间 */
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        /** 线程等待队列 */
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(30);
        /** 线程创建工厂 */
        ThreadFactory threadFactory = new ClientTreadFactory();
        /** 拒绝策略 */
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
            Thread t = new Thread(r, "aix-client-t" + mThreadNum.getAndIncrement());
            ClientLogUtils.info(t.getName() + " has been created");
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
            ClientLogUtils.error(r.toString() + " rejected from " + e.toString(),true);
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }
}
