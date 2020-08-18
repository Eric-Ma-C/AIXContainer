package org.zju.vipa.aix.container.center.db.service;

import org.apache.ibatis.session.SqlSession;
import org.zju.vipa.aix.container.center.db.dao.DataturksUserDAO;
import org.zju.vipa.aix.container.center.db.dao.DeviceDAO;
import org.zju.vipa.aix.container.center.db.dao.ModelDAO;
import org.zju.vipa.aix.container.center.db.dao.TaskDAO;
import org.zju.vipa.aix.container.center.db.entity.DataturksUser;
import org.zju.vipa.aix.container.center.db.entity.Device;
import org.zju.vipa.aix.container.center.db.entity.Model;
import org.zju.vipa.aix.container.common.config.Config;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.entity.Task;

import java.util.List;

/**
 * @Date: 2020/7/31 10:30
 * @Author: EricMa
 * @Description: aix  db操作具体实现
 */
public class AixDbServiceImpl extends SqlSessionInitializer implements DbService {

    @Override
    public SqlSession getSession() {
        return super.getSession();
    }

    @Override
    public void closeSession() {
         super.closeSession();
    }

    @Override
    public Boolean updateDeviceGpuDetailById(String clientId, String detail) {
        // 获取映射类
        DeviceDAO deviceDAO = getSession().getMapper(DeviceDAO.class);
        deviceDAO.updateDetailById(clientId,detail);

        return true;
    }

    @Override
    public Boolean setTaskFinished(String taskId) {
        // 获取映射类
        TaskDAO taskDAO = getSession().getMapper(TaskDAO.class);
        taskDAO.setTaskStatus(taskId, "FINISHED");

        return true;
    }

    @Override
    public Boolean setTaskFailed(String taskId) {
        // 获取映射类
        TaskDAO taskDAO = getSession().getMapper(TaskDAO.class);
        taskDAO.setTaskStatus(taskId, "FAILED");

        return true;
    }

    @Override
    public Task grabTask(String clientId) {
        SqlSession sqlSession=getSession();

        // 获取映射类
        TaskDAO taskDAO = sqlSession.getMapper(TaskDAO.class);
        List<Task> taskList = taskDAO.findWaittingList();
        if (taskList == null || taskList.size() == 0) {
            /** No waiting task,grab failed */
            return null;
        }
        Task task = taskList.get(0);
        taskDAO.taskTobeTrained(task.getId(),  clientId);


//                //  获取code path
        if (DebugConfig.IS_DOWNLOAD_MODULE) {
            //下载模型,路径为模型解压目录
            task.setCodePath(Config.MODEL_UNZIP_PATH);
        } else {
            //数据库中的路径
            ModelDAO modelDAO = sqlSession.getMapper(ModelDAO.class);
            Model model = modelDAO.findModelById(task.getModelId());
            String codePath = model.getCodePath();
            task.setCodePath(codePath);
        }


        return task;
    }

    @Override
    public String getClientIdByToken(String token) {
        DeviceDAO deviceDAO = getSession().getMapper(DeviceDAO.class);
        Device device = deviceDAO.getDeviceByToken(token);
        if (device == null) {
            return null;
        } else {
            return device.getId();
        }
    }

    @Override
    public List<Task> getWaittingTaskList() {
        // 获取映射类
        TaskDAO taskDAO = getSession().getMapper(TaskDAO.class);
        // 直接调用接口的方法，传入参数id=1，返回Student对象
        List<Task> taskList = taskDAO.findWaittingList();

        return taskList;
    }

    @Override
    public List<Task> getTaskList() {
        // 获取映射类
        TaskDAO taskDAO = getSession().getMapper(TaskDAO.class);
        // 直接调用接口的方法，传入参数id=1，返回Student对象
        List<Task> taskList = taskDAO.findAllList();

        return taskList;
    }

    @Override
    public List<DataturksUser> getDataturksUserList() {
        DataturksUserDAO dataturksUserDao = getSession().getMapper(DataturksUserDAO.class);
        // 直接调用接口的方法，传入参数id=1，返回Student对象
        return dataturksUserDao.getUserList();
    }

    @Override
    public void updateDeviceLastLoginById(String clientId) {
        //不用实现
    }
}
