package org.zju.vipa.aix.container.client.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.zju.vipa.aix.container.client.network.ClientMessage;
import org.zju.vipa.aix.container.client.network.UploadDataListener;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.config.NetworkConfig;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
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
        /** 设置初始化Handler */
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
            ClientLogUtils.error("连接Server(IP={},PORT={})失败,InterruptedException={}", host, port,e);
            Thread.currentThread().interrupt();
        }
        return channel;
    }

    /**
     * 同步方法，向服务器发送消息
     *
     * @param sendData 待发送字符串
     * @return: void
     */
    public void sendMsg(String sendData) throws InterruptedException {
        Channel channel = getChannel(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT_LISTENING);
        if (channel == null) {
            ClientLogUtils.debug("Netty建立连接失败!");
            return;
        }
        try {
            channel.writeAndFlush(sendData).sync();
            if (DebugConfig.CLIENT_NETWORK_IO_LOG) {
                ClientLogUtils.debug("SEND MSG:{}", sendData);
            }
        } finally {
            channel.close().sync();
        }


    }

    /**
     * 同步方法，向服务器发送消息
     */
    public Message sendMsgAndGetResponse(String sendData, int timeout) throws InterruptedException {
        Channel channel = getChannel(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT_LISTENING);
        if (channel == null) {
            ClientLogUtils.error("Netty建立连接失败!");
            return null;
        }
        Message message = null;
        try {
            /** 初始化阻塞拦截器 */
            CountDownLatch latch = new CountDownLatch(1);
            ClientInboundMsgHandler handler = (ClientInboundMsgHandler) channel.pipeline().get(ClientInboundMsgHandler.NAME);
            handler.setLatch(latch);

            /** 写数据 */
            try {
                channel.writeAndFlush(sendData).sync();
            } catch (Exception e) {
                ClientExceptionUtils.handle(e);
            }
            if (DebugConfig.CLIENT_NETWORK_IO_LOG) {
                ClientLogUtils.debug("SEND MSG:{}", sendData);
            }

            /** 阻塞等待 */
            latch.await(timeout, TimeUnit.MILLISECONDS);

            /** 收到返回数据，或者超时 */
            message = handler.getResponse();

        } finally {
            channel.close().sync();
        }

        return message;
    }

    /**
     * 同步方法，向服务器上传文件
     * FileRegion在linux上支持零拷贝
     */
    public void uploadFile(String filePath, final UploadDataListener listener) throws InterruptedException {
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            ClientLogUtils.info(true, "Uploading file {} is not exists.", filePath);
            return;
        }

        /** 注意打开的是SERVER_PORT_FILE_UPLOAD */
        final Channel channel = getChannel(NetworkConfig.SERVER_IP, NetworkConfig.SERVER_PORT_FILE_UPLOAD);
        if (channel == null) {
            ClientLogUtils.error("Netty建立连接失败!");
            return;
        }

        long fileLenBytes = file.length();

        /** 先发一个Message，通知服务器存大文件（日志）*/
        Message uploadMsg = new ClientMessage(Intent.UPLOAD_DATA, filePath);
        uploadMsg.addCustomData("size", String.valueOf(fileLenBytes));
        byte[] uploadMsgBytes = JsonUtils.toJSONString(uploadMsg).getBytes(StandardCharsets.UTF_8);

        /** 添加Message的长度 */
//        CompositeByteBuf uploadMsgBuf = Unpooled.compositeBuffer();
        CompositeByteBuf uploadMsgBuf = channel.alloc().compositeBuffer(2);

        uploadMsgBuf.addComponents(Unpooled.copyShort(uploadMsgBytes.length), Unpooled.wrappedBuffer(uploadMsgBytes));
        uploadMsgBuf.writerIndex(2 + uploadMsgBytes.length);
        /** 跳过所有编码器发送 */
        channel.pipeline().firstContext().writeAndFlush(uploadMsgBuf).sync();

//        ChunkedStream chunkedStream = new ChunkedStream(fileInputStream);

        /** 准备开始上传文件 */
        ClientLogUtils.info("Uploading File size {} bytes", fileLenBytes);
        FileRegion region = new DefaultFileRegion(fileInputStream.getChannel(), 0, fileLenBytes);

        /** 跳过所有编码器发送 */
        ChannelFuture future = channel.pipeline().firstContext().writeAndFlush(region);


//        // 新建CompositeByteBuf对象
//        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
//// 第一个参数是true, 表示当添加新的ByteBuf时, 自动递增 CompositeByteBuf 的 writeIndex。如果不传第一个参数或第一个参数为false，则合并后的compositeByteBuf的writeIndex不移动，即不能从compositeByteBuf中读取到新合并的数据。
//        ByteBuf fileBuf = Unpooled.wrappedBuffer(fileInputStream., (int) fileInputStream.count(),false);
//        compositeByteBuf.addComponents(true, lenBuf, fileBuf);
//        /** 再写文件 */
////        ChannelFuture future = channel.writeAndFlush(compositeByteBuf);
//        ChannelFuture future = channel.pipeline().firstContext().writeAndFlush(compositeByteBuf);
//
//
//


//        future.addListener(new tim)
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                /** todo 啥时候超时 */
                if (future.isSuccess()) {
                    //todo 收到服务器确认才成功
                    listener.onSuccess();
                } else {
                    listener.onError(future.cause());
                }
                channel.close().sync();
            }
        });
//        future.sync();


//        /**  设置读(服务器接收成功消息)超时时间,上传不容易，多等一下成功确认 */
//        int uploadTimeOut = 30 * 60 * 1000;
//        /** 阻塞等待 */
//        latch.await(uploadTimeOut, TimeUnit.MILLISECONDS);
//
//        /** 收到返回数据，或者超时 */
//        message = handler.getResponse();


    }

}
