package zju.vipa.container.center.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Date: 2020/1/7 15:44
 * @Author: EricMa
 * @Description: 启动socket监听
 */
public class CenterTcpServer {
        /** 最大同时处理socket数量,即线程池的大小 */
        private static final int MAX_SOCKET_NUM=100;

        public static void start(int port) throws IOException {
                //创建Socket对象
                ServerSocket serverSocket = new ServerSocket(port);

                /** 线程池，防止并发过高时创建过多线程耗尽资源 */
                //todo 手动创建线程池
                ExecutorService threadPool = Executors.newFixedThreadPool(MAX_SOCKET_NUM);

                while (true) {
                        //server尝试接收其他Socket的连接请求，accept方法是阻塞式的
                        /** 短连接方式,类似http */
                        Socket socket = serverSocket.accept();
                        //每接收到一个Socket请求就建立一个新的线程来处理它
                        threadPool.submit(new SocketHandler(socket));
                }

        }

}
