package org.zju.vipa.aix.container.center.netty;

import io.netty.channel.ChannelHandlerContext;
import org.zju.vipa.aix.container.center.network.ServerIO;
import org.zju.vipa.aix.container.message.Message;
import org.zju.vipa.aix.container.utils.JsonUtils;

/**
 * @Date: 2020/5/7 12:56
 * @Author: EricMa
 * @Description:
 */
public class NettyIoImpl implements ServerIO {


    ChannelHandlerContext context;

    public NettyIoImpl(ChannelHandlerContext context) {
        this.context = context;
    }

    @Override
    public void response(Message msg) {
        String data= JsonUtils.toJSONString(msg);
//        context.write(Unpooled.copiedBuffer(, CharsetUtil.UTF_8));
        context.writeAndFlush(data);
        context.channel().close();
    }


    @Override
    public void saveData(Message msg) {
      /** todo */
    }
}
