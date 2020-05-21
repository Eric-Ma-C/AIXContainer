package org.zju.vipa.aix.container.center.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Date: 2020/5/12 13:27
 * @Author: EricMa
 * @Description: 初始化连接时的组件
 */
public class MyWebSocketChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("http-codec",new HttpServerCodec());
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
        ch.pipeline().addLast("handler",new MyWebSocketHandler());
    }
}
