package zju.vipa.container.center.network;

import zju.vipa.container.utils.LogUtils;

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
public class CenterTcpServer {
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

    private static void initThreadPool() {

        /** 核心线程池大小 */
        int corePoolSize = 4;
        /** 最大线程池大小 */
        int maximumPoolSize = 10;
        /** 线程最大空闲时间 */
        long keepAliveTime = 20;
        TimeUnit unit = TimeUnit.SECONDS;
        /** 线程等待队列 */
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(30);
        /** 线程创建工厂 */
        ThreadFactory threadFactory = new AIXTreadFactory();
        /** 拒绝策略 */
        RejectedExecutionHandler handler = new AIXIgnorePolicy();


        mThreadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize,
            keepAliveTime, unit,
            workQueue, threadFactory, handler);

        // 预启动所有核心线程
        mThreadPoolExecutor.prestartAllCoreThreads();
    }

    public static void start(int port) throws IOException {
        /** 创建线程池，防止并发过高时创建过多线程耗尽资源 */
        initThreadPool();

        //创建Socket对象
        mServerSocket = new ServerSocket(port);


        while (true) {
            //server尝试接收其他Socket的连接请求，accept方法是阻塞式的
            /** 短连接方式,类似http 2.0 */
            Socket socket = mServerSocket.accept();
            //每接收到一个Socket请求就建立一个新的线程来处理它
            mThreadPoolExecutor.execute(new SocketHandler(socket));
        }

    }

        /** 可以更改线程的名称，线程组，优先级，守护进程状态等 */
    static class AIXTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "aix-thread-" + mThreadNum.getAndIncrement());
            LogUtils.info(t.getName() + " has been created");
            return t;
        }
    }

    public static class AIXIgnorePolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        /**
         * 可做日志记录等
         */
        private void doLog(Runnable r, ThreadPoolExecutor e) {
            //
            LogUtils.error(r.toString() + " rejected");
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }

}
