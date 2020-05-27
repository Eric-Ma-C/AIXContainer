package org.zju.vipa.aix.container.client.netty.tcp;

import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import org.zju.vipa.aix.container.client.network.ClientIO;
import org.zju.vipa.aix.container.client.network.ClientMessage;
import org.zju.vipa.aix.container.client.network.UploadDataListener;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.config.DebugConfig;
import org.zju.vipa.aix.container.config.NetworkConfig;
import org.zju.vipa.aix.container.message.Intent;
import org.zju.vipa.aix.container.message.Message;
import org.zju.vipa.aix.container.utils.JsonUtils;

import java.io.*;

/**
 * @Date: 2020/5/6 16:49
 * @Author: EricMa
 * @Description: 基于netty的tcp传输
 */
public class NettyIoImpl implements ClientIO {

    NettyTcpClient client;

    public NettyIoImpl() {
        client = new NettyTcpClient();
    }

    @Override
    public Message sendMsgAndGetResponse(ClientMessage message) {
        return sendMsgAndGetResponse(message, NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);
    }

    @Override
    public Message sendMsgAndGetResponse(ClientMessage message, int readTimeOut) {

        ClientLogUtils.debug("SEND:\n{}\n", message);

        if (DebugConfig.IS_LOCAL_DEBUG) {
            readTimeOut = DebugConfig.SOCKET_READ_TIMEOUT_DEBUG;
        }
        Message response = null;

        String sendData = JsonUtils.toJSONString(message);

        try {
            response = client.sendMsgAndGetResponse(sendData, readTimeOut);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return response;
    }

    @Override
    public void sendMessage(ClientMessage message) {
        if (!Intent.SHELL_ERROR_HELP.match(message) && !Intent.EXCEPTION.match(message)) {
            //EXCEPTION已经在日志中记录过了
            ClientLogUtils.debug("SEND:\n{}\n", message);
        }

        String sendData = JsonUtils.toJSONString(message);

//        boolean doLog =!Intent.SHELL_ERROR_HELP.match(message);
        try {
            client.sendMsg(sendData);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean upLoadData(String path, UploadDataListener listener) {
        /** 设置读取超时时间,上传不容易，多等一下成功确认 */
        int readTimeOut = 30 * 1000;

        if (DebugConfig.IS_LOCAL_DEBUG) {
            readTimeOut = DebugConfig.SOCKET_READ_TIMEOUT_DEBUG;
        }

        File file=null;
        FileInputStream dataInput = null;
        try {
            file=new File(path);
            dataInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            ClientLogUtils.info(true, "Uploading file {} is not exists.", path);
            return false;
        }

        FileRegion region=new DefaultFileRegion(dataInput.getChannel(),0,file.length());


//        /**  读取文件到内存缓冲区 */
//        BufferedInputStream dataInputStream = new BufferedInputStream(dataInput);


        /** 先发一个Message，通知服务器存大文件（日志）*/
        Message uploadMsg = new ClientMessage(Intent.UPLOAD_DATA, path);
        String uploadMsgData = JsonUtils.toJSONString(uploadMsg);

        Message response = null;
        try {

            client.sendMsg(uploadMsgData);

            ClientLogUtils.info("开始上传文件：{}", path);


            response = client.uploadData(region,listener);

            ClientLogUtils.info("文件{} 上传完毕，等待服务器确认", path);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        if (response == null) {
            /** 若服务端没有回应 */
//            listener.onError("上传完成后,服务端没有回应");
            return false;
        }
        if (Intent.UPLOAD_SUCCESS.match(response)) {
            listener.onSuccess();
        }
        return true;





        /** 发送缓冲区 */
//            BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

//            /** tcp MSS=1460 */
//            byte[] bys = new byte[1460];
//            int len;
//            while ((len = dataInputStream.read(bys)) != -1) {
//                outputStream.write(bys, 0, len);
//                outputStream.flush();
//            }
//            dataInputStream.close();


    }


////    private Channel channel;
//
//    public NettyIoImpl() {
//        init();
//        start();
//    }
//
//    private void init() {
//        clientBootstrap = new Bootstrap();
//        group = new NioEventLoopGroup();
//        clientBootstrap.group(group).option(ChannelOption.SO_KEEPALIVE, true)
//            .channel(NioSocketChannel.class)
//            .handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                protected void initChannel(SocketChannel socketChannel) throws Exception {
//                    //将双方约定好的分隔符转成buf
//                    ChannelPipeline pipeline = socketChannel.pipeline();
//
//                    ByteBuf delimiter = Unpooled.copiedBuffer(Message.END_STRING.getBytes(Message.CHARSET_NAME));
//                    pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, delimiter));
//                    //字符串编码解码
//                    pipeline.addLast("decoder", new StringDecoder());
//                    pipeline.addLast("encoder", new StringEncoder());
//                    //心跳检测
//                    pipeline.addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
//                    //客户端的逻辑
//                    pipeline.addLast("handler", new TcpClientHandler(NettyIoImpl.this));
//
//                }
//            });
//    }
//
//    protected void start() {
//        /** 连接 */
//        ChannelFuture f = clientBootstrap.connect(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT);
//        //断线重连
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                if (!channelFuture.isSuccess()) {
//                    final EventLoop loop = channelFuture.channel().eventLoop();
//                    loop.schedule(new Runnable() {
//                        @Override
//                        public void run() {
//                            ClientLogUtils.error("not connect service");
//                            start();
//                        }
//                    }, 1L, TimeUnit.SECONDS);
//                } else {
//                    channel = channelFuture.channel();
//                    ClientLogUtils.info("连接AIX服务器成功");
//                }
//            }
//        });
//    }
//
////    public Channel getChannel() {
////        return channel;
////    }
//
//
//    /**
//     * 关闭netty
//     */
//    public void close() {
//
//        try {
//            group.shutdownGracefully().sync();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//    }
//
//    @Override
//    public Message sendMsgAndGetResponse(ClientMessage message) {
//        return sendMsgAndGetResponse(message, NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);
//    }
//
//    /**
//     * 同步非阻塞方法
//     *
//     * @param message     待发送消息
//     * @param readTimeOut 超时时间
//     * @return: zju.vipa.aix.container.message.Message
//     */
//    @Override
//    public Message sendMsgAndGetResponse(ClientMessage message, int readTimeOut) {
//        // Start the client.
//        /**
//         * wait()方法：Waits for this future to be completed.
//         * Waits for this future until it is done, and rethrows the cause of the failure if this future
//         * failed.
//         */
////        long t1 = System.currentTimeMillis();
////        ChannelFuture f = b.connect(host, port).await();
////        // Wait until the connection is closed.
////        f.channel().closeFuture().await();    //closeFuture方法返回通道关闭的结果
////        long t2 = System.currentTimeMillis();
////        System.out.print("diff in seconds:" + (t2 - t1) / 1000 + "\n");
//
//
//        ChannelFuture channelFuture = null;
//        try {
//            channelFuture = clientBootstrap.connect().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        }
//        // 发送请求
//        channelFuture.channel().write(message);
//
//        channelFuture.channel().flush();
//
//
////        channelFuture.channel().closeFuture().sync();
//
//
//        return msg;
//    }
//
//    @Override
//    public void sendMessage(ClientMessage message) {
//
//    }
//
//    @Override
//    public boolean upLoadData(String path, UploadDataListener listener) {
//        return false;
//    }
}
