package zju.vipa.aix.container.client.network;

import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.utils.TokenUtils;

/**
 * @Date: 2020/3/30 21:26
 * @Author: EricMa
 * @Description: 容器消息
 */
public class ClientMessage extends Message {
        private static String token= TokenUtils.getDeviceToken();

    public ClientMessage(Intent intent, String value) {
        super(intent,value,token);
    }

    public ClientMessage(Intent intent) {
        super(intent,token);
    }
}
