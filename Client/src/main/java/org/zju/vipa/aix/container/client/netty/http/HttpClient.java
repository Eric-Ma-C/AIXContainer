package org.zju.vipa.aix.container.client.netty.http;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.HttpDownloadUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Date: 2020/4/30 10:22
 * @Author: EricMa
 * @Description: 基于netty的http clint
 */
public class HttpClient {
    private static class HttpClientHolder {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    private HttpClient() {
    }

    public static HttpClient getInstance() {
        return HttpClientHolder.INSTANCE;
    }

    public void get(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpClientInboundHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            URI uri = new URI("https://" + host + ":" + port);
//            URI uri = new URI();
            String msg = "Are you ok?";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

            // 构建http请求
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            // 发送http请求
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    public void get(String url) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpClientInboundHandler());
                }
            });

            String host = url.substring(url.indexOf("//") + 2);
            host = host.substring(0, host.indexOf('/'));
            url = url.substring(url.indexOf('/', 8));
            // Start the client.区分 80 443?
            ChannelFuture f;
//            if (url.startsWith("http")){
//                f=b.connect(host,80).sync();
//            }else {
            f = b.connect(host, 80).sync();
//            }

            URI uri = new URI(url);
//            String msg = "Are you ok?";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                uri.toASCIIString());

            // 构建http请求
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            // 发送http请求
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }


    /**
     * 下载http资源 向服务器下载直接填写要下载的文件的相对路径
     * （↑↑↑建议只使用字母和数字对特殊字符对字符进行部分过滤可能导致异常↑↑↑）
     * 向互联网下载输入完整路径
     *
     * @param host     目的主机ip或域名
     * @param port     目标主机端口
     * @param url      文件路径
     * @param savePath 本地存储路径
     * @throws Exception
     */
    public void getDownload(String host, int port, String url, final String savePath, final HttpDownloadUtils.DownloadListener listener) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /** 引导类 */
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            /** tcp */
            b.channel(NioSocketChannel.class);
//         todo 可以不要？   b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new ChunkedWriteHandler());
                    ch.pipeline().addLast(new HttpDownloadHandler(savePath, listener));
                }
            });
//            b.handler(new HttpsCodecInitializer());

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();


            URI uri = null;
            uri = new URI(url);

            DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString());

            // 构建http请求
            request.headers().set(HttpHeaderNames.HOST, host + ":" + port);
            request.headers().set(HttpHeaderNames.CONNECTION,
                HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.ACCEPT,
                "*/*");

            request.headers().set("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTExMTExMTExMTExMTExMTExMTEifQ.it6iBaBmEkYIkJ49_2dCUL6nSqH7SHTJJ2IpoM8-nAs");

            // 发送http请求
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (URISyntaxException e) {
            ClientExceptionUtils.handle(e);
        } catch (Exception e) {
            /** 其他异常，如网络连接失败 */
            ClientExceptionUtils.handle(e,false);
            if (listener != null&&e.getMessage().contains("Connection refused")) {
                listener.onError(-1);
            }

        } finally {
            workerGroup.shutdownGracefully();
        }

    }


//    public static void main(String[] args) throws Exception {
//        HttpClient client = new HttpClient();
////        client.connect("www.baidu.com", 80);
////        client.connect("https://cs.nju.edu.cn/shiyh/DM2017/parts/28.pdf");
////        client.getDownload("cs.nju.edu.cn", 80, "https://cs.nju.edu.cn/shiyh/DM2017/parts/28.pdf", "欧拉图.pdf");
////        client.getDownload("techforum-img.cn-hangzhou.oss-pub.aliyun-inc.com", 80, "http://techforum-img.cn-hangzhou.oss-pub.aliyun-inc.com/1523849261680/AliTech101_RD.pdf", "AliTech101_RD.pdf");
//        client.getDownload("10.214.211.205", 8080, "http://10.214.211.205:8080/model/36e0a2906cd642438e83f7bf9443855c/file", "model.zip");
////        client.connect("zlysix.gree.com", 80, "http://zlysix.gree.com/HelloWeb/download/20m.apk", "20m.apk");
//
//
//    }
}
