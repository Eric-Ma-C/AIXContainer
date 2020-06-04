package org.zju.vipa.aix.container.center.dubbo;

import org.zju.vipa.aix.container.api.AIXClientCenterService;
import org.zju.vipa.aix.container.center.log.LogReader;
import org.zju.vipa.aix.container.center.manager.TaskManagerService;
import org.zju.vipa.aix.container.common.entity.Task;
import org.zju.vipa.aix.container.common.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @Date: 2020/5/31 10:40
 * @Author: EricMa
 * @Description: 接口实现
 */
public class AIXClientCenterServiceImpl implements AIXClientCenterService {
    @Override
    public int getOnlineNum() {
//        return NettyTcpServer.group.size();
//        return 7654321;
        return (int) (Math.random() * 100);
    }

    @Override
    public String srverLogReadToEnd() {
        return LogReader.readToEnd();
    }

    @Override
    public void srverLogInit() {
        LogReader.readInit();
    }

    @Override
    public void srverLogStop() {
        LogReader.stop();
    }

    @Override
    public int getActiveClientNum() {
        return TaskManagerService.getActiveClientNum();
    }

    @Override
    public List<Message> getMessageQueueByToken(String token) {
        Queue<Message> queue = TaskManagerService.getMessageQueueByToken(token);
        return new ArrayList<>(queue);
    }

    @Override
    public Task getTaskByToken(String token) {
        return TaskManagerService.getTaskByToken(token);
    }


}
