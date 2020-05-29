package org.zju.vipa.aix.container.center.netty.upload;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.zju.vipa.aix.container.center.log.ClientLogFileManager;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.config.DebugConfig;
import org.zju.vipa.aix.container.message.Message;
import org.zju.vipa.aix.container.utils.JsonUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Date: 2020/5/27 20:56
 * @Author: EricMa
 * @Description: 处理上传文件
 */
public class UploadFileHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * file info is at the begining of the data frame
     */
    private boolean isGetFileInfo = false;
    /**
     * the uploading file infomation
     */
    private Message fileInfoMsg;
    /**
     * the uploading file
     */
    private File saveFile;
    private FileOutputStream fos;
    /**
     * file size in bytes
     */
    long fileLen;
    /**
     * current file writer index
     */
    long fileWriterIndex = 0L;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (!isGetFileInfo) {
            /** 保存待上传文件信息 */
            short msgLen = msg.readShort();
            byte[] msgBytes = new byte[msgLen];

            msg.readBytes(msgBytes);
            String fileInfoMsgStr = new String(msgBytes, CharsetUtil.UTF_8);
            fileInfoMsg = JsonUtils.parseObject(fileInfoMsgStr, Message.class);

            isGetFileInfo = true;
//        new SocketHandler(ctx).handle(fileInfoMsg);
        } else {
            /** 保存文件 */
            if (saveFile == null) {
                saveFile = ClientLogFileManager.getInstence().getSavePath(fileInfoMsg.getToken(), fileInfoMsg.getValue());
                fileLen = Long.valueOf(fileInfoMsg.getCustomData("size"));
                fos = new FileOutputStream(saveFile);
            }


            //            RandomAccessFile randomAccessFile=new RandomAccessFile(saveFile, "rw");

//            FileRegion region=new DefaultFileRegion(fos.getChannel(),0,fileLen);
            int tmpSize = 0;
            while ((tmpSize = msg.readBytes(fos.getChannel(), fileWriterIndex, msg.readableBytes())) > 0) {
                fileWriterIndex += tmpSize;
            }

            fos.flush();

        }
    }


//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (DebugConfig.OPEN_NETTY_LOG) {
//            LogUtils.info("UploadFileHandler 读取内容{}", msg);
//        }
//
////        if (msg instanceof FileRegion)
//
//
//    }
//
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (DebugConfig.OPEN_NETTY_LOG) {
            LogUtils.info("UploadFileHandler {}:channelReadComplete",ctx.channel().id());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        /** 关闭文件输出流 */
        fos.close();
        if (DebugConfig.OPEN_NETTY_LOG) {
            LogUtils.info("UploadFileHandler {}:channelInactive",ctx.channel().id());
        }
    }

    //
//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//
//    }


}
