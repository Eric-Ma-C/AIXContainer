package org.zju.vipa.aix.container.client.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.HttpDownloadUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Date: 2020/4/30 11:54
 * @Author: EricMa
 * @Description: http下载文件
 */
public class HttpDownloadHandler extends ChannelInboundHandlerAdapter {
    /**
     * 分块读取开关
     */
    private boolean readingChunks = false;
    /**
     * 文件输出流
     */
    private FileOutputStream fOutputStream = null;
    /**
     * 下载文件的本地对象
     */
    private File localfile = null;
    /**
     * 待下载文件名
     */
    private String savePath = null;
    /**
     * 状态码
     */
    private int succCode;

    HttpDownloadUtils.DownloadListener listener;

    public HttpDownloadHandler(String savePath, HttpDownloadUtils.DownloadListener listener) {
        this.savePath = savePath;
        this.listener=listener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception {
        // response头信息
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            succCode = response.status().code();
            if (succCode == 200) {
                /**设置下载文件*/
                setDownLoadFile();
                readingChunks = true;
            }else{
                if (listener!=null){
                    listener.onError(succCode);
                }
            }
            ClientLogUtils.info("CONTENT_TYPE:{}", response.headers().get(HttpHeaderNames.CONTENT_TYPE));
        }
        // response体信息
        if (msg instanceof HttpContent) {
            HttpContent chunk = (HttpContent) msg;
            if (chunk instanceof LastHttpContent) {
                readingChunks = false;
            }

            ByteBuf buffer = chunk.content();
            byte[] dst = new byte[buffer.readableBytes()];
            if (succCode == 200) {
                while (buffer.isReadable()) {
                    buffer.readBytes(dst);
                    fOutputStream.write(dst);
                    buffer.release();
                }
                if (null != fOutputStream) {
                    fOutputStream.flush();
                }
            }else{
                if (listener!=null){
                    listener.onError(succCode);
                }
            }

        }
        if (!readingChunks) {
            if (null != fOutputStream) {
                ClientLogUtils.info("Download done->" + localfile.getAbsolutePath());
                fOutputStream.flush();
                fOutputStream.close();
                localfile = null;
                fOutputStream = null;
            }
            ctx.channel().close();
        }
    }

    /**
     * 配置本地参数，准备下载
     */
    private void setDownLoadFile() throws Exception {
        if (null == fOutputStream) {
//            savePath = SystemPropertyUtil.get("user.dir") + File.separator + savePath;
            localfile = new File(savePath);
            if (!localfile.exists()) {
                localfile.createNewFile();
            }
            fOutputStream = new FileOutputStream(localfile);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ClientExceptionUtils.handle("管道异常：{}", cause, true);
        ctx.channel().close();
        if (listener!=null){
            listener.onError(succCode);
        }
    }
}
