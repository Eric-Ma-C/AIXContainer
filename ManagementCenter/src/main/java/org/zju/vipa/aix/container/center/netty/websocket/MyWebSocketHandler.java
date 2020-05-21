package org.zju.vipa.aix.container.center.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;

import java.util.Date;

/**
 * @Date: 2020/5/12 10:24
 * @Author: EricMa
 * @Description: 接收，处理，响应客户端请求
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;
    private static final String WEB_SOCKET_URL = "ws://localhost:8888/websocket";

    /**
     * 处理客户端发来的消息 netty5中改名为messageReceived()
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            /** 处理tcp握手请求 */
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            /** 处理websocket连接请求 */
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /**
     * 处理客户端向服务端发起http握手请求
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess() || !("websocket".equals(req.headers().get("Upgrade")))) {
            /** 不是http握手请求 */
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory =
            new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }

    }

    /**
     * 向客户端发送响应消息
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        if (res.status().code() != 200) {
            /** 请求失败处理 */
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        /** 向客户端返回消息 */
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (res.status().code() != 200) {
            /** 关闭连接 */
            f.addListener(ChannelFutureListener.CLOSE);
        }


    }

    /**
     * 处理客户端与服务端之间的websocket业务
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            /** 关闭websocket指令 */
            handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
        }
        if (frame instanceof PingWebSocketFrame) {
            /** 处理ping消息 */
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            /** 二进制消息 */
            LogUtils.worning("暂不支持二进制消息");
            throw new RuntimeException(this.getClass().getName() + "不支持消息");
        }
        /** 返回应答消息，获取客户端向服务端发送的消息 */
        String request = ((TextWebSocketFrame) frame).text();
        LogUtils.debug("收到客户端消息：{}", request);
        TextWebSocketFrame tws = new TextWebSocketFrame(
            new Date().toString() + ctx.channel().id() +
                "===>>>" + request);
        /** 向每个客户端群发消息 */
        NettyConfig.group.writeAndFlush(tws);

    }


    /**
     * 服务端接收客户端数据结束时调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    /**
     * 客户端与服务端创建连接时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.add(ctx.channel());
        LogUtils.info("客户端与服务端连接开启");
    }

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.remove(ctx.channel());
        LogUtils.info("客户端与服务端连接断开");
    }


    /**
     * 工程出现异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ExceptionUtils.handle(cause);
        ctx.close();
    }
}
