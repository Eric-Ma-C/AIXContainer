package org.zju.vipa.aix.container.client.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Date: 2020/5/27 20:11
 * @Author: EricMa
 * @Description: 判断消息类型
 *  目前的类型：String（Message） FileReigon
 */
@Deprecated
public class AIXMessageTypeHandler extends MessageToByteEncoder<ByteBuf> {
public static final String name="AIXMessageTypeHandler";

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {

    }
}
