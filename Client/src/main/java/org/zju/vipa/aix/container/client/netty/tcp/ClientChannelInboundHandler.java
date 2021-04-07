package org.zju.vipa.aix.container.client.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.utils.ProtostuffUtils;

import java.util.concurrent.CountDownLatch;

/**
 * @Date: 2021/4/7 14:29
 * @Author: EricMa
 * @Description:
 */
public class ClientChannelInboundHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME ="ClientInboundMsgHandler";
    /**
     * ref http://tutorials.jenkov.com/netty/netty-tcp-client.html
     */

//    private ChannelListener channelListener;
    private Message response = null;
    private CountDownLatch latch = null;


    /**
     * 打开channel
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        NettyTcpClient.channelGroup.add(ctx.channel());
        if (DebugConfig.OPEN_NETTY_LOG){
            ClientLogUtils.debug("channel:{}连接开启", ctx.channel().id());
        }
    }

    /**
     * 关闭channel
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
//        NettyTcpClient.channelGroup.remove(ctx.channel());
        if (DebugConfig.OPEN_NETTY_LOG) {
            ClientLogUtils.debug("channel:{}连接关闭", ctx.channel().id());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ByteBuf) {
            /** 处理客户端请求 */
            //json反序列化
//        response  =  JsonUtils.parseObject((String) msg, Message.class);
            //pb反序列化
            ByteBuf byteBuf= (ByteBuf) msg;
            byte[] data;
            if (byteBuf.hasArray()){
                data=byteBuf.array();
                if (DebugConfig.OPEN_NETTY_LOG) {
                    ClientLogUtils.debug("RECEIVE HeapBuf MSG FROM {} :\n{}\n", ctx.channel().id(), data);
                }
            }else {
                int len=byteBuf.readableBytes();
                data=new byte[len];
                byteBuf.getBytes(byteBuf.readerIndex(),data);
                if (DebugConfig.OPEN_NETTY_LOG) {
                    ClientLogUtils.debug("RECEIVE DirectBuf MSG FROM {} :\n{}\n", ctx.channel().id(), data);
                }
            }

            response  = ProtostuffUtils.deserialize(data, Message.class);


        } else {
            ClientLogUtils.error("\n\nUnknown msg {}\n\n", msg);
        }

        if (latch!=null){
            latch.countDown();
        }

        ReferenceCountUtil.release(msg);
    }

    /**
     * 服务端接收客户端数据结束时调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (DebugConfig.OPEN_NETTY_LOG) {
            ClientLogUtils.info("channel:{}读取完成", ctx.channel().id());
        }
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //netty 一般涉及网络错误，不上传
//        ClientExceptionUtils.handle(cause,false);
        ClientExceptionUtils.handle(cause);
        ctx.close();
//        cause.printStackTrace();

    }

    public Message getResponse() {
        return response;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}
