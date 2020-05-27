package org.zju.vipa.aix.container.center.network;

import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.config.NetworkConfig;
import org.zju.vipa.aix.container.exception.AIXBaseException;
import org.zju.vipa.aix.container.exception.ExceptionCodeEnum;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Date: 2020/1/7 15:44
 * @Author: EricMa
 * @Description: 启动socket监听
 */
public class SocketServer {
    /**
     * 最大同时处理socket数量,即线程池的大小
     */
    private static final int MAX_SOCKET_NUM = 200;
    /**
     * 线程池
     */
    private static ThreadPoolExecutor mThreadPoolExecutor;
    /**
     * ServerSocket
     */
    private static ServerSocket mServerSocket;

    private static class CenterTcpServerHolder {
        private static final SocketServer INSTANCE = new SocketServer();
    }

    private SocketServer() {
        if (CenterTcpServerHolder.INSTANCE!=null){
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }
    }

    public static SocketServer getInstance() {
        return CenterTcpServerHolder.INSTANCE;
    }


    /**
     * 初始化线程池，注意根据服务器硬件参数修改线程池参数
     *
     * @return: void
     */
    private static void initThreadPool() {
        /** 核心线程池大小 */
        int corePoolSize = 20;
        /** 最大线程池大小 */
        int maximumPoolSize = 250;
        /** 线程最大空闲时间 */
        long keepAliveTime = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        /** 线程等待队列 */
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(500);
        /** 线程创建工厂 */
        ThreadFactory threadFactory = new AIXThreadFactory();
        /** 拒绝策略 */
        RejectedExecutionHandler handler = new AIXIgnorePolicy();


        mThreadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize,
            keepAliveTime, unit,
            workQueue, threadFactory, handler);

        // 预启动所有核心线程
        mThreadPoolExecutor.prestartAllCoreThreads();


//        mThreadPoolExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    System.out.println();
//                    System.out.println();
//
//                    int queueSize = mThreadPoolExecutor.getQueue().size();
//                    if(queueSize>400) {
//                        LogUtils.debug("当前排队线程数：" + queueSize);
//                    }
//
//                    int activeCount = mThreadPoolExecutor.getActiveCount();
//                    if(activeCount>20) {
//                        LogUtils.debug("当前活动线程数：" + activeCount);
//                    }
//
////                    long completedTaskCount = mThreadPoolExecutor.getCompletedTaskCount();
////                    LogUtils.debug("执行完成线程数：" + completedTaskCount);
//
////                    long taskCount = mThreadPoolExecutor.getTaskCount();
////                    LogUtils.debug("总线程数：" + taskCount);
//
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//                }
//            }
//        });

    }

    public void start() throws IOException {
        /** 创建线程池，防止并发过高时创建过多线程耗尽资源 */
        initThreadPool();

        //创建Socket对象
        mServerSocket = new ServerSocket(NetworkConfig.SERVER_PORT);


        while (true) {
            //server尝试接收其他Socket的连接请求，accept方法是阻塞式的
            Socket socket = mServerSocket.accept();
            //每接收到一个Socket请求就建立一个新的线程来处理它
            //todo 抽离业务代码
            mThreadPoolExecutor.execute(new SocketHandler(socket));
//            LogUtils.debug("Handle new request: {}", socket.toString());
        }

    }

    /**
     * 可以更改线程的名称，线程组，优先级，守护进程状态等
     */
    private static class AIXThreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "st" + mThreadNum.getAndIncrement());
            LogUtils.info("{} has been created", t.getName());
            return t;
        }
    }

    /**
     * 拒绝策略
     */
    private static class AIXIgnorePolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            /** 直接拒绝不执行新任务 */
            doLog(r, e);
        }

        /**
         * 可做日志记录等
         */
        private void doLog(Runnable r, ThreadPoolExecutor e) {
            /** 服务器处理线程池满了，压测的时候注意一下 */
            LogUtils.error("\n\n/***************** THREADPOOL WORNING *****************/\n{} rejected from {}\n\n\n", r.toString(), e.toString());
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }

}
