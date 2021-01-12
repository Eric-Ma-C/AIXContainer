package org.zju.vipa.aix.container.center.db.service;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.zju.vipa.aix.container.center.db.dao.atlas.AixDeviceMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.CodesMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.TaskTaskMapper;
import org.zju.vipa.aix.container.center.log.LogUtils;
import org.zju.vipa.aix.container.common.config.Config;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.db.entity.aix.DataturksUser;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;
import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;
import org.zju.vipa.aix.container.common.db.entity.atlas.Codes;
import org.zju.vipa.aix.container.common.db.entity.atlas.TaskTask;

import java.util.ArrayList;
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
        super.closeSession();
    }

    @Override
    public Boolean updateDeviceGpuDetailById(String clientId, String detail) {
        // 获取映射类
        AixDeviceMapper aixDeviceMapper = getSession().getMapper(AixDeviceMapper.class);
        int rec = aixDeviceMapper.updateDetailById(Integer.parseInt(clientId), detail);

        return rec > 0;
    }

    @Override
    public Boolean updateDeviceTokenById(String clientId, String token) {
        // 获取映射类
        AixDeviceMapper aixDeviceMapper = getSession().getMapper(AixDeviceMapper.class);
        int rec = aixDeviceMapper.updateTokenById(Integer.parseInt(clientId), token);

//        System.out.println("id="+clientId+" rec="+rec);

        /** rec == 0没有该id的记录 */
        return rec > 0;

    }

    @Override
    public Boolean updateDeviceNameById(String clientId, String name) {
        // 获取映射类
        AixDeviceMapper aixDeviceMapper = getSession().getMapper(AixDeviceMapper.class);
        int rec = aixDeviceMapper.updateNameById(Integer.parseInt(clientId), name);

//        System.out.println("id="+clientId+" rec="+rec);

        /** rec == 0没有该id的记录 */
        return rec > 0;
    }

    @Override
    public Boolean setTaskFinished(String taskId) {
        // 获取映射类
        TaskTaskMapper atlasTaskMapper = getSession().getMapper(TaskTaskMapper.class);
        int rec = atlasTaskMapper.setTaskStatus(Integer.parseInt(taskId), "FINISHED");
        return rec > 0;
    }

    @Override
    public Boolean setTaskFailedById(String taskId) {
        // 获取映射类
        TaskTaskMapper atlasTaskMapper = getSession().getMapper(TaskTaskMapper.class);
        int rec = atlasTaskMapper.setTaskStatus(Integer.parseInt(taskId), "FAILED");
        return rec > 0;
    }

    @Override
    public Boolean setTaskWaitingById(String taskId) {
        // 获取映射类
        TaskTaskMapper atlasTaskMapper = getSession().getMapper(TaskTaskMapper.class);
        int rec = atlasTaskMapper.setTaskStatus(Integer.parseInt(taskId), "WAITING");
        return rec > 0;
    }

    @Override
    public Task grabTask(String clientId) {
        SqlSession sqlSession = getSession();

        LogUtils.debug("grabTask sqlSession={}", sqlSession);
        // 获取映射类
        TaskTaskMapper taskMapper = sqlSession.getMapper(TaskTaskMapper.class);
        List<TaskTask> taskList = taskMapper.findWaittingList();
        int len = taskList.size();
        if (taskList == null || len == 0) {
            /** No waiting task,grab failed */
            return null;
        }

//        for (TaskTask task : taskList) {
//            System.out.println(Thread.currentThread().getId()+":"+task.getName());
//        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        TaskTask atlasTask = taskList.get((int) (System.currentTimeMillis() % len));
        int count = taskMapper.taskTobeTrained(atlasTask.getId(), Integer.parseInt(clientId));
        if (count == 0) {
            /**  乐观锁检测出  多线程导致的事务并发冲突，抢任务失败  没有写数据库所以不需要回滚，故不需要抛异常 */
            return null;
        }


        //将atlas task转为aix task
        Task task = new Task(atlasTask);
        task.setStatus("TRAINING");

        //  获取code path
        if (DebugConfig.IS_DOWNLOAD_MODULE) {
            //下载模型,路径为模型解压目录
            task.setCodePath(Config.MODEL_UNZIP_PATH);
        } else {
            //数据库中的路径
//            ModelsMapper modelsMapper = sqlSession.getMapper(ModelsMapper.class);
//            Models atlasModel = modelsMapper.findModelById(Integer.parseInt(task.getModelId()));
//            String codePath = atlasModel.getFile();
//            task.setCodePath(codePath);
            CodesMapper codesMapper = sqlSession.getMapper(CodesMapper.class);
            Codes atlasCodes = codesMapper.findCodesById(Integer.parseInt(task.getModelId()));
            if (atlasCodes == null) {
                LogUtils.error("Codes表中没有任务记录");
            }
            String codePath = atlasCodes.getFile();
            task.setCodePath(codePath);
            String preCmd = atlasCodes.getPre_cmd();
            task.setPreCmds(preCmd);
        }


//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return task;
    }

    @Override
    public String getClientIdByToken(String token) {
        AixDeviceMapper deviceMapper = getSession().getMapper(AixDeviceMapper.class);
        AixDevice device = deviceMapper.getDeviceByToken(token);
        if (device == null) {
            return null;
        } else {
            return device.getId() + "";
        }
    }

    @Override
    public AixDevice getClientByToken(String token) {
        AixDeviceMapper deviceMapper = getSession().getMapper(AixDeviceMapper.class);
        AixDevice device = deviceMapper.getDeviceByToken(token);
        if (device == null) {
            return null;
        } else {
            return device;
        }
    }


    @Override
    public void insertClient(AixDevice aixDevice) {
        AixDeviceMapper deviceMapper = getSession().getMapper(AixDeviceMapper.class);
        deviceMapper.insert_aix_device(aixDevice.getDevice_name(), aixDevice.getInfo(), aixDevice.getToken(), aixDevice.getUser_id());

    }

    @Override
    public AixDevice getClientById(String id) {
        AixDeviceMapper deviceMapper = getSession().getMapper(AixDeviceMapper.class);
        AixDevice device = deviceMapper.selectById(Integer.valueOf(id));
        if (device == null) {
            return null;
        } else {
            return device;
        }
    }

    @Override
    public List<AixDevice> getClientListByPage(int page,int countPerPage) {
        AixDeviceMapper deviceMapper = getSession().getMapper(AixDeviceMapper.class);
        RowBounds rowBounds=new RowBounds((page-1)*countPerPage,countPerPage);
        List<AixDevice> deviceList = deviceMapper.selectByPage(rowBounds);

        return deviceList;
    }

    @Override
    public int getClientCount() {
        AixDeviceMapper deviceMapper = getSession().getMapper(AixDeviceMapper.class);
        int rows = deviceMapper.selectCount();

        return rows;
    }


    @Override
    public List<Task> getWaittingTaskList() {
        // 获取映射类
        TaskTaskMapper atlasTaskMapper = getSession().getMapper(TaskTaskMapper.class);
        // 直接调用接口的方法，传入参数id=1，返回Student对象
        List<TaskTask> atlasTaskList = atlasTaskMapper.findWaittingList();

        List<Task> taskList = new ArrayList<>();
        for (TaskTask atlasTask : atlasTaskList) {
            taskList.add(new Task(atlasTask));
        }

        return taskList;
    }

    @Override
    public List<Task> getTaskList() {
        // 获取映射类
        TaskTaskMapper atlasTaskMapper = getSession().getMapper(TaskTaskMapper.class);
        // 直接调用接口的方法，传入参数id=1，返回Student对象
        List<TaskTask> atlasTaskList = atlasTaskMapper.findAllList();

        List<Task> taskList = new ArrayList<>();
        for (TaskTask atlasTask : atlasTaskList) {
            taskList.add(new Task(atlasTask));
        }

        return taskList;
    }

    @Override
    public List<DataturksUser> getDataturksUserList() {
        return null;
    }

    @Override
    public void updateDeviceLastLoginById(String clientId) {
        // 获取映射类
        AixDeviceMapper aixDeviceMapper = getSession().getMapper(AixDeviceMapper.class);
        aixDeviceMapper.updateLastLoginById(Integer.parseInt(clientId));
    }


}
