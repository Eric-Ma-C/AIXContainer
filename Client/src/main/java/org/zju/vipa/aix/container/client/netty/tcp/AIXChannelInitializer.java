package org.zju.vipa.aix.container.client.netty.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @Date: 2020/5/13 16:02
 * @Author: EricMa
 * @Description: todo:
 */
public class AIXChannelInitializer extends ChannelInitializer<Channel> {


    @Override
    protected void initChannel(Channel ch) throws Exception {

        // 每个 `Channel` 都关联一个 `ChannelPipeline`

        /* 发送和接收的 `object`通过`ObjectDecoder` `ObjectEncoder`进行加解密
         * 注：对应`object`类,必须实现`Serializable`接口
         *
         * `netty`框架本身自带了很多`Encode`和`DeCode`
         *  例如：字符串的 `StringDecoder` `StringEncoder`
         */

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//        pipeline.addLast(new ObjectEncoder());
//        pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("tcpClientHandler", new TcpClientHandler());
    }

}
