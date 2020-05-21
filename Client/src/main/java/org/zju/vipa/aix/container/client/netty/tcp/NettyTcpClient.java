package org.zju.vipa.aix.container.client.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.config.NetworkConfig;
import org.zju.vipa.aix.container.message.Message;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2020/5/12 20:48
 * @Author: EricMa
 * @Description: netty客户端
 */
public class NettyTcpClient {
    //    private EventLoopGroup group;
    private static Bootstrap bootstrap = getBootstrap();
//    private static Channel channel;

    /**
     * 客户端与服务端已建立的所有连接，由于串行处理事务，通常情况下只有一个
     */
//    public static ChannelGroup channelGroup;
    protected NettyTcpClient() {
//        init();
    }


    /**
     * 初始化 Bootstrap
     */
    private static final Bootstrap getBootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        /** 设置超时 */
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);

        b.handler(new AIXChannelInitializer());
//        b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    // 建立连接
    private final Channel getChannel(String host, int port) {
        Channel channel = null;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ClientLogUtils.error("连接Server(IP{},PORT{})失败", host, port);
        }
        return channel;
    }

    /**
     * 同步方法，向服务器发送消息
     *
     * @param sendData 待发送字符串
     * @param doLog    是否记录日志
     * @return: void
     */
    public void sendMsg(String sendData, boolean doLog) throws InterruptedException {
        Channel channel = getChannel(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT);

        try {
            if (channel == null) {
                ClientLogUtils.debug("Netty建立连接失败!");
                return;
            }
            channel.writeAndFlush(sendData).sync();
            if (doLog){
                ClientLogUtils.info("SEND MSG:{}", sendData);
            }
        } finally {
            channel.close().sync();
        }

    }

    /**
     * 同步方法，向服务器发送消息
     */
    public Message sendMsgAndGetResponse(String sendData, int timeout) throws InterruptedException {
        Channel channel = getChannel(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT);
        Message message = null;
        try {
            if (channel == null) {
                ClientLogUtils.debug("Netty建立连接失败!");
                return null;
            }
            /** 初始化阻塞拦截器 */
            CountDownLatch latch = new CountDownLatch(1);
            TcpClientHandler handler = (TcpClientHandler) channel.pipeline().get("tcpClientHandler");
            handler.setLatch(latch);

            /** 写数据 */
            try {
                channel.writeAndFlush(sendData).sync();
            } catch (Exception e) {
                ClientExceptionUtils.handle(e);
            }

            ClientLogUtils.info("SEND MSG:{}", sendData);

            /** 阻塞等待 */
            latch.await(timeout, TimeUnit.MILLISECONDS);

            /** 收到返回数据，或者超时 */
            message = handler.getResponse();

        } finally {
            channel.close().sync();
        }

        return message;
    }


//    private void init(){
//        group = new NioEventLoopGroup();
//        try{
//            clientBootstrap = new Bootstrap();
//
//            clientBootstrap.group(group);
//            clientBootstrap.channel(NioSocketChannel.class);
//            clientBootstrap.remoteAddress(new InetSocketAddress(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT));
//            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                protected void initChannel(SocketChannel socketChannel) throws Exception {
//                    socketChannel.pipeline().addLast(new TcpClientHandler());
//                }
//            });
//
//
//            ChannelFuture channelFuture = clientBootstrap.connect().sync();
//            channelFuture.channel().closeFuture().sync();
//
//        } catch (InterruptedException e){
//            Thread.currentThread().interrupt();
//        }finally {
//
//            group.shutdownGracefully();
//
//        }
//    }
}
