package org.zju.vipa.aix.container.center.netty.upload;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Date: 2020/5/27 21:31
 * @Author: EricMa
 * @Description: 0527 todo:第一个字节代表消息类型
 */
@Deprecated
public class MessageTypeHandler extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
