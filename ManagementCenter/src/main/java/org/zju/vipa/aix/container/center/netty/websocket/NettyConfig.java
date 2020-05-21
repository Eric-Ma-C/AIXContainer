package org.zju.vipa.aix.container.center.netty.websocket;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Date: 2020/5/12 10:19
 * @Author: EricMa
 * @Description: 全局配置
 */
public class NettyConfig {
    /** 存储客户端连接进来的channel */
    public static ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
