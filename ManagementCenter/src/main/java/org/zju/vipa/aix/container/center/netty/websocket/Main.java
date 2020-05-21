package org.zju.vipa.aix.container.center.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;

/**
 * @Date: 2020/5/12 13:35
 * @Author: EricMa
 * @Description: 测试入口，启动
 */
public class Main {
    public static void main(String[] args) {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new MyWebSocketChannelHandler());
            LogUtils.info("服务端开启等待客户端连接...");

            Channel ch=bootstrap.bind(8888).sync().channel();
            ch.closeFuture().sync();



        }catch (Exception e){
            ExceptionUtils.handle(e);
        }finally {
            /** 退出程序 */
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
