package org.zju.vipa.aix.container.client.netty.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.util.concurrent.CountDownLatch;

/**
 * @Date: 2020/5/6 16:53
 * @Author: EricMa
 * @Description: 每次打开的channel都会在这处理
 */
public class ClientInboundMsgHandler extends SimpleChannelInboundHandler<String> {

    public static final String name="ClientInboundMsgHandler";
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
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        response = JsonUtils.parseObject(msg,Message.class);

        if (latch!=null){
            latch.countDown();
        }


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //netty 一般涉及网络错误，不上传
//        ClientExceptionUtils.handle(cause,false);
        ClientExceptionUtils.handle(cause);
        ctx.close();
//        cause.printStackTrace();
//        ctx.close();
    }

    public Message getResponse() {
        return response;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    //
//    public void setChannelListener(ChannelListener channelListener) {
//        this.channelListener = channelListener;
//    }
//
//    public interface ChannelListener {
//        void onChannelRead(Message msg);
//}


//    private NettyIoImpl nettyIo;
//    private String tenantId;
//    private int attempts = 0;
//
//    public TcpClientHandler(NettyIoImpl nettyIo){
//        this.nettyIo = nettyIo;
//    }
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("output connected!");
//        attempts = 0;
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        ClientLogUtils.error("TCP连接中断");
//        //使用过程中断线重连
//        final EventLoop eventLoop = ctx.channel().eventLoop();
//        if (attempts<12){
//            attempts++;
//        }
//        int timeout = 2<<attempts;
//        eventLoop.schedule(new Runnable() {
//            @Override
//            public void run() {
//                nettyIo.start();
//            }
//        },timeout, TimeUnit.SECONDS);
//        ctx.fireChannelInactive();
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent){
//            IdleStateEvent event = (IdleStateEvent)evt;
//            if (event.state().equals(IdleState.READER_IDLE)){
//                System.out.println("READER_IDLE");
//            }else if (event.state().equals(IdleState.WRITER_IDLE)){
//                //发送心跳，保持长连接
//                String s = "NettyClient"+System.getProperty("line.separator");
//                ctx.channel().writeAndFlush(s);  //发送心跳成功
//            }else if (event.state().equals(IdleState.ALL_IDLE)){
//                System.out.println("ALL_IDLE");
//            }
//        }
//        super.userEventTriggered(ctx,evt);
//    }
//
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
//        channelHandlerContext.close();
//        /** 异常处理 */
//        ClientExceptionUtils.handle(cause);
//
//    }
//
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
//        ClientLogUtils.info(msg.toString());
//    }


}
