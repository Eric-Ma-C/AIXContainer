package zju.vipa.aix.container.center.network;

import zju.vipa.aix.container.config.Config;
import zju.vipa.aix.container.config.NetworkConfig;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;

/**
 * @Date: 2020/3/30 21:36
 * @Author: EricMa
 * @Description: 服务器消息
 */
public class ServerMessage extends Message {
//    private static String token= NetworkConfig.CENTER_TOKEN;
    private static String token= "aix center";


    public ServerMessage(Intent intent, String value) {
        super(intent, value, token);
    }

    public ServerMessage(Intent intent) {
        super(intent, token);
    }
}
