package org.zju.vipa.aix.container.center.netty;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zju.vipa.aix.container.center.network.ServerIO;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.config.DebugConfig;
import org.zju.vipa.aix.container.message.Message;
import org.zju.vipa.aix.container.utils.JsonUtils;

/**
 * @Date: 2020/5/7 12:56
 * @Author: EricMa
 * @Description:
 */
public class NettyIoImpl implements ServerIO {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyIoImpl.class);

    ChannelHandlerContext context;

    public NettyIoImpl(ChannelHandlerContext context) {
        this.context = context;
    }

    @Override
    public void response(Message msg,boolean isClose) {
        if (DebugConfig.SERVER_NET_IO_LOG) {
            LogUtils.debug("RESPONSE TO {}:\n{}\n", context.channel().remoteAddress(), msg);
          //todo  LOGGER.debug("RESPONSE TO {}:\n{}\n", context.channel().remoteAddress(), msg);
        }

        String data= JsonUtils.toJSONString(msg);
//        context.write(Unpooled.copiedBuffer(, CharsetUtil.UTF_8));
        context.writeAndFlush(data);
        if (isClose) {
            context.channel().close();
        }
    }

    @Override
    public void response(Message msg) {
        response(msg,true);
    }


    @Override
    public void saveData(Message msg) {

        LogUtils.info("\n\nsaveData\n\n");


        context.read();


//        File saveFile = ClientLogFileManager.getInstence().getSavePath(msg.getToken(), msg.getValue());

//        try {
//
//            FileRegion saveRegion=new DefaultFileRegio
//            context.rea
//
//
//
//            // 封装通道内流
//            FileInputStream in;
//            // 封装文件
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
//
//            byte[] bys = new byte[1024 * 4];
//            int len;
//            while ((len = inputStream.read(bys)) != -1) {
//                bos.write(bys, 0, len);
//                bos.flush();
//            }
//
////            bis.close();
//            bos.close();
//        } catch (IOException e) {
//            ExceptionUtils.handle(e);
//        }
//
//        LogUtils.info("文件{}接收成功。", saveFile.getPath());
//        // 反馈上传成功
//        response(new ServerMessage(Intent.UPLOAD_SUCCESS));
    }
}
