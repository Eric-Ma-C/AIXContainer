package org.zju.vipa.aix.container.center.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.config.NetworkConfig;

/**
 * @Date: 2020/5/6 22:40
 * @Author: EricMa
 * @Description: client请求几乎都是短连接，更适合AIX训练平台的模式
 */
public class NettyTcpServer {
    /**
     * 存储客户端连接进来的channel
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public static void start() {
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("bossGroup", true));
//        EventLoopGroup workGroup = new NioEventLoopGroup(8, new DefaultThreadFactory("workGroup", true));

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);

            bootstrap.channel(NioServerSocketChannel.class);
//            bootstrap.localAddress(new InetSocketAddress(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT));
//            bootstrap.localAddress(new InetSocketAddress("localhost", NetworkConfig.SERVER_PORT));
            /** 如果不设置超时，连接会一直占用本地线程，端口，连接客户端一多，会导致本地端口用尽及CPU压力 */
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NetworkConfig.SOCKET_READ_TIMEOUT_DEFAULT);


            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline=socketChannel.pipeline();
                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                    pipeline.addLast(new ObjectEncoder());
//                    pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new NettyServerHandler());
                }
            });

            LogUtils.info("Netty服务端开启等待Client连接...");

            ChannelFuture channelFuture = bootstrap.bind(NetworkConfig.SERVER_PORT).sync();
            /** 等待channel的关闭,会阻塞在这里 */
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            ExceptionUtils.handle(e);
        } finally {

            bossGroup.shutdownGracefully();
//            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully();


        }
    }


//
//    //维护设备在线的表
//    private Map<String, Integer> clientMap = new HashMap<>();
//    //维护设备连接的map 用于推送消息
//    private Map<String, Channel> channelMap = new HashMap<>();
//
//
//    public synchronized void setClient(String name) {
//        this.clientMap.put(name, 1);
//    }
//
//    public synchronized void removeClient(String name) {
//        this.clientMap.remove(name);
//    }
//
//    //判断连接表里面是否有东西
//    public synchronized boolean getClientMapSize() {
//        return this.clientMap.size() > 0;
//    }
//
//
//
//    public synchronized void setChannel(String name, Channel channel) {
//        this.channelMap.put(name, channel);
//    }
//
//    public synchronized Map<String, Channel> getChannelMap() {
//        return this.channelMap;
//    }
//
//    //发送消息给下游设备
//    public boolean writeMsg(String msg) {
//        boolean errorFlag = false;
//        Map<String, Channel> channelMap = getChannelMap();
//        if (channelMap.size() == 0) {
//            return true;
//        }
//        Set<String> keySet = clientMap.keySet();
//        for (String key : keySet) {
//            try {
//                Channel channel = channelMap.get(key);
//                if (!channel.isActive()) {
//                    errorFlag = true;
//                    continue;
//                }
//                channel.writeAndFlush(msg + System.getProperty("line.separator"));
//            } catch (Exception e) {
//                errorFlag = true;
//            }
//        }
//        return errorFlag;
//    }
//
//    public void bind() {
//        System.out.println("service start successful");
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        ServerBootstrap bootstrap = new ServerBootstrap();
//        bootstrap.group(bossGroup, workerGroup)
//            .channel(NioServerSocketChannel.class)
//            .childOption(ChannelOption.SO_KEEPALIVE, true)
//            .option(ChannelOption.SO_BACKLOG, 1024)
//            .childHandler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                protected void initChannel(SocketChannel socketChannel) throws Exception {
//                    ChannelPipeline pipeline = socketChannel.pipeline();
//                    //特殊分隔符
//                    pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,
//                        Unpooled.copiedBuffer(System.getProperty("line.separator").getBytes())));
//                    pipeline.addLast("decoder", new StringDecoder());
//                    pipeline.addLast("encoder", new StringEncoder());
//                    pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
//                    pipeline.addLast("handler", new NettyServerHandler(NettyTcpServer.this));
//                }
//            });
//        try {
//            ChannelFuture f = bootstrap.bind(NetworkConfig.SERVER_PORT).sync();
//            f.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
//
//    public static void main(String[] args) {
//        final NettyTcpServer nettyServer = new NettyTcpServer();
//        new Thread() {
//            @Override
//            public void run() {
//                nettyServer.bind();
//            }
//        }.start();
//        Scanner scanner = new Scanner(System.in);
//        String msg = "";
//        while (!(msg = scanner.nextLine()).equals("exit")) {
//            System.out.println(nettyServer.writeMsg(msg));
//        }
//    }
}
