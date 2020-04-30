package zju.vipa.aix.container.client.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @Date: 2020/4/30 13:09
 * @Author: EricMa
 * @Description: todo:
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {

//    private final SslContext context;

    public HttpsCodecInitializer() {
//        this.context = context;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
//        p.addLast（" ssl"， sslCtx.newHandler(channel.alloc(), host, port) ）;

        ChannelPipeline pipeline = ch.pipeline();
        SslContext sslContext= SslContextBuilder.forClient().build();
        SSLEngine engine = sslContext.newEngine(ch.alloc());
        pipeline.addFirst("ssl", new SslHandler(engine));  //1

        pipeline.addLast("codec", new HttpClientCodec());  //2


    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
//                    ch.pipeline().addLast(new HttpResponseDecoder());
//                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
//                    ch.pipeline().addLast(new HttpRequestEncoder());
//                    ch.pipeline().addLast(new ChunkedWriteHandler());
//                    ch.pipeline().addLast(new HttpDownloadHandler(savePath));
    }
}
