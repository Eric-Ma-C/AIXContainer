package org.zju.vipa.aix.container.center.manager;

import org.zju.vipa.aix.container.common.entity.Task;
import org.zju.vipa.aix.container.common.message.Message;

import java.util.Queue;

/**
 * @Date: 2020/6/4 18:47
 * @Author: EricMa
 * @Description: TaskManager对外接口
 */
public class TaskManagerService {
    /**
     * 正在与平台通信的训练容器数量
     * 也就是待发送队列的数量
     */
    public static int getActiveClientNum() {
        return TaskManager.getInstance().getActiveClientNum();
    }

    /**
     * 获取某容器的待发送队列
     */
    public static Queue<Message> getMessageQueueByToken(String token) {
        return TaskManager.getInstance().getMessageQueueByToken(token);
    }

    /**
     * 获取某容器的当前任务
     */
    public static Task getTaskByToken(String token) {
        return TaskManager.getInstance().getTaskByToken(token);
    }

}
