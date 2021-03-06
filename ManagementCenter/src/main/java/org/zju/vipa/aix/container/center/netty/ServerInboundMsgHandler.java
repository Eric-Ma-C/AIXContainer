package org.zju.vipa.aix.container.center.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.zju.vipa.aix.container.center.network.SocketHandler;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

/**
 * @Date: 2020/5/6 22:45
 * @Author: EricMa
 * @Description:
 */
public class ServerInboundMsgHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理客户端发来的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (DebugConfig.OPEN_NETTY_LOG) {
            LogUtils.info("客户端channel:{}读取内容{}", ctx.channel().id(),msg);
        }

        if (msg instanceof String) {
            /** 处理客户端请求 */
            Message receivedMessage = JsonUtils.parseObject((String) msg, Message.class);
            if (DebugConfig.SERVER_NETWORK_IO_LOG) {
                /** 收到客户端日志 */
                LogUtils.debug("RECEIVE MSG FROM {} TOKEN: {}:\n{}\n", ctx.channel().id(), receivedMessage.getTokenSuffix(), msg);
            }

//            if (Intent.UPLOAD_DATA.match(receivedMessage.getIntent())){
//                /** 上传文件 */
//                LogUtils.info("准备读取文件内容");
//            }

            new SocketHandler(ctx).handle(receivedMessage);

        }
//        else if (msg instanceof FileRegion) {
//
//            FileRegion region = (FileRegion) msg;
//
//            LogUtils.info("\n\nmsg instanceof FileRegion\n\n");
//
//        } else if (msg instanceof ByteBuf){
//
//            LogUtils.error("\n\nmsg instanceof ByteBuf\n\n", msg);
//
//
//        }
        else {
            LogUtils.error("\n\nUnknown msg {}\n\n", msg);
        }


        /** todo 验证是否需要释放资源 */
        ReferenceCountUtil.release(msg);
    }














    /**
     * 服务端接收客户端数据结束时调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//            .addListener(ChannelFutureListener.CLOSE);
        if (DebugConfig.OPEN_NETTY_LOG) {
            LogUtils.info("客户端channel:{}读取完成", ctx.channel().id());
        }

        ctx.flush();
    }

    /**
     * 客户端与服务端创建连接时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyTcpServer.group.add(ctx.channel());
        if (DebugConfig.OPEN_NETTY_LOG) {
            LogUtils.info("客户端channel:{}与服务端连接开启", ctx.channel().id());
        }
    }

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyTcpServer.group.remove(ctx.channel());
        if (DebugConfig.OPEN_NETTY_LOG) {
            LogUtils.info("客户端channel:{}与服务端连接断开", ctx.channel().id());
        }
    }

    /**
     * netty出现异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (DebugConfig.OPEN_NETTY_LOG) {
            LogUtils.info("客户端channel:{}发生异常", ctx.channel().id());
        }
        ExceptionUtils.handle(cause);
        ctx.close();
    }


//    private int counter=0;
//    private NettyTcpServer nettyServer;
//
//    public NettyServerHandler(NettyTcpServer nettyServer){
//        this.nettyServer = nettyServer;
//    }
//
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //重置心跳次数
//        counter = 0;
//
//        String body = (String) msg;
//        System.out.println("The time server receive order : " + body + "; the counter is : " + ++counter);
//        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
//        currentTime = currentTime + System.getProperty("line.separator");
//        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
//        ctx.writeAndFlush(resp);
//
//
//
////        ByteBuf inBuffer = (ByteBuf) msg;
////        String received = inBuffer.toString(CharsetUtil.UTF_8);
////        System.out.println("Server received: " + received);
////        ctx.write(Unpooled.copiedBuffer("Hello " + received, CharsetUtil.UTF_8));
//    }
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        String clientName = ctx.channel().remoteAddress().toString();
//        System.out.println("RemoteAddress:"+clientName+"active!");
//        nettyServer.setClient(clientName);
//        nettyServer.setChannel(clientName,ctx.channel());
//        super.channelActive(ctx);
//        counter = 0;
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent){
//            IdleStateEvent event = (IdleStateEvent)evt;
//            if (event.state().equals(IdleState.READER_IDLE)){
//                //空闲4s后触发
//                if (counter>=10){
//                    ctx.channel().close().sync();
//                    String clientName = ctx.channel().remoteAddress().toString();
//                    System.out.println(""+clientName+"offline");
//                    nettyServer.removeClient(clientName);
//                    //判断是否有在线的
//                    if (nettyServer.getClientMapSize()){
//                        return;
//                    }
//                }else{
//                    counter++;
//                    System.out.println("loss"+counter+"count HB");
//                }
//            }
//        }
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//            .addListener(ChannelFutureListener.CLOSE);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ExceptionUtils.handle(cause);
//        ctx.close();
//    }

}
