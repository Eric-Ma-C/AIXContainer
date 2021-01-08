package org.zju.vipa.aix.container.center.netty.upload;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.log.LogUtils;
import org.zju.vipa.aix.container.common.config.NetworkConfig;

/**
 * @Date: 2020/5/28 19:20
 * @Author: EricMa
 * @Description: 文件上传服务器
 * 与message服务器分开，
 */
public class FileUploadServer {
    /**
     * 存储客户端连接进来的channel
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public static void start() {
        /** 可以比message服务器少分配一点线程资源 */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("bossGroup", true));
        EventLoopGroup workGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("workGroup", true));


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

                    /** 处理文件上传 */
                    pipeline.addLast(new UploadFileHandler());

                }
            });

            LogUtils.info("Netty File Upload服务端开启等待Client连接...");

            ChannelFuture channelFuture = bootstrap.bind(NetworkConfig.SERVER_PORT_FILE_UPLOAD).sync();
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

}

