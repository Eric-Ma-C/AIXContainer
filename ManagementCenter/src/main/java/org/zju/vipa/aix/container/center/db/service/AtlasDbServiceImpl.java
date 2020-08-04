package org.zju.vipa.aix.container.center.db.service;

import org.apache.ibatis.session.SqlSession;
import org.zju.vipa.aix.container.center.db.entity.DataturksUser;
import org.zju.vipa.aix.container.common.entity.Task;

import java.util.List;

/**
 * @Date: 2020/8/4 15:56
 * @Author: EricMa
 * @Description: 三合一项目  atlas数据库访问
 */
public class AtlasDbServiceImpl extends SqlSessionInitializer implements DbService {
    @Override
    public SqlSession getSession() {
        return super.getSession();
    }

    @Override
    public void closeSession() {

    }

    @Override
    public Boolean updateDeviceGpuDetailById(String clientId, String detail) {
        return null;
    }

    @Override
    public Boolean setTaskFinished(String taskId) {
        return null;
    }

    @Override
    public Task grabTask(String clientId) {
        return null;
    }

    @Override
    public String getClientIdByToken(String token) {
        return null;
    }

    @Override
    public List<Task> getWaittingTaskList() {
        return null;
    }

    @Override
    public List<Task> getTaskList() {
        return null;
    }

    @Override
    public List<DataturksUser> getDataturksUserList() {
        return null;
    }
}
