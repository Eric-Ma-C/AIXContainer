package zju.vipa.aix.container.client.network;

import zju.vipa.aix.container.client.Client;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.client.utils.TokenUtils;

/**
 * @Date: 2020/3/30 21:26
 * @Author: EricMa
 * @Description: 容器消息
 */
public class ClientMessage extends Message {

    public ClientMessage(Intent intent, String value) {
        super(intent, value, Client.TOKEN);
    }

    public ClientMessage(Intent intent) {
        super(intent, Client.TOKEN);
    }

    @Override
    public String toString() {
        return "Message{" +
            "intent=" + intent +
            ", value='" + value + '\'' +
            ", customDataMap=" + customDataMap +
            '}';
    }
}
