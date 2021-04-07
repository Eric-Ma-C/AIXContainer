package org.zju.vipa.aix.container.client.network;

import org.zju.vipa.aix.container.client.Client;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;

import java.util.Arrays;

/**
 * @Date: 2020/3/30 21:26
 * @Author: EricMa
 * @Description: 容器消息
 */
public class ClientMessage extends Message {

    public ClientMessage(Intent intent, String value) {
        super(intent, value, Client.TOKEN);
    }

    public ClientMessage(Intent intent, byte[] byteValue) {
        super(intent, byteValue, Client.TOKEN);
    }

    public ClientMessage(Intent intent) {
        super(intent, Client.TOKEN);
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
            "intent=" + intent +
            ", value='" + value + '\'' +
            ", byteValue=" + Arrays.toString(byteValue) +
            ", customDataMap=" + customDataMap +
            '}';
    }
}
