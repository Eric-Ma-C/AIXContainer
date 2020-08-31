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
import org.zju.vipa.aix.container.center.netty.upload.FileUploadServer;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.common.config.NetworkConfig;

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
        //主reactor
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //从reactor
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


                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8));
                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(8));
//                    pipeline.addLast(new ObjectEncoder());
//                    pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));


//                    pipeline.addLast(new );

                    /** 处理String类型的message */
                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));



                    pipeline.addLast(new ServerInboundMsgHandler());

//                    pipeline.addLast(new UploadFileHandler());
//                    pipeline.addLast(new ());


                }
            });

            LogUtils.info("Netty服务端开启等待Client连接...");

            ChannelFuture channelFuture = bootstrap.bind(NetworkConfig.SERVER_PORT_LISTENING).sync();

            /** 启动文件上传服务器 */
            FileUploadServer.start();


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
