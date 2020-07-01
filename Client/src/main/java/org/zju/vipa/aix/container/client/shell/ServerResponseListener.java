package org.zju.vipa.aix.container.client.shell;
import org.zju.vipa.aix.container.common.message.Message;

/**
 * @Date: 2020/7/1 10:14
 * @Author: EricMa
 * @Description: 任务执行完毕拿到服务器响应后的回调
 */

public interface ServerResponseListener {
    void onResponse(Message message);
}
