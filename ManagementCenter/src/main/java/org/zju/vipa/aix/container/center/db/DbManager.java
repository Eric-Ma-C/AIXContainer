package org.zju.vipa.aix.container.center.db;

import org.apache.ibatis.session.SqlSession;
import org.zju.vipa.aix.container.center.db.dao.DataturksUserDAO;
import org.zju.vipa.aix.container.center.db.dao.TaskDAO;
import org.zju.vipa.aix.container.center.db.entity.DataturksUser;
import org.zju.vipa.aix.container.center.db.dao.DeviceDAO;
import org.zju.vipa.aix.container.center.db.dao.ModelDAO;
import org.zju.vipa.aix.container.center.db.entity.Device;
import org.zju.vipa.aix.container.center.db.entity.Model;
import org.zju.vipa.aix.container.common.entity.Task;
import org.zju.vipa.aix.container.common.config.Config;
import org.zju.vipa.aix.container.common.config.DebugConfig;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @Date: 2020/1/9 21:07
 * @Author: EricMa
 * @Description: 数据库管理器, 单例
 */
public class DbManager implements Serializable {

    //    private SqlSessionFactory sqlSessionFactory;
    private org.apache.ibatis.session.SqlSessionManager sqlSessionManager;


    private static class DbManagerHolder {
        private static final DbManager INSTANCE = new DbManager();
    }

    private DbManager() {
        if (DbManagerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }

        init();
    }

    public static DbManager getInstance() {
        return DbManagerHolder.INSTANCE;
    }


    /**
     * 初始化
     */
    private void init() {

    }


    public List<DataturksUser> getDataturksUserList() {
        return SqlSessionProxy.start(new SqlTask<List<DataturksUser>>() {
            @Override
            public List<DataturksUser> exec(SqlSession sqlSession) {
                DataturksUserDAO dataturksUserDao = sqlSession.getMapper(DataturksUserDAO.class);
                // 直接调用接口的方法，传入参数id=1，返回Student对象
                return dataturksUserDao.getUserList();
            }
        });

//        List<DataturksUser> users= SqlSessionManager.getSession().getMapper()
    }

    /**
     * 获取所有任务
     *
     * @param
     * @return: java.util.List<Task>列表
     */
    public List<Task> getTaskList() {
        return SqlSessionProxy.start(new SqlTask<List<Task>>() {
            @Override
            public List<Task> exec(SqlSession sqlSession) {
                // 获取映射类
                TaskDAO taskDAO = sqlSession.getMapper(TaskDAO.class);
                // 直接调用接口的方法，传入参数id=1，返回Student对象
                List<Task> taskList = taskDAO.findAllList();

                return taskList;
            }
        });

    }

    /**
     * 获取等待状态的任务列表，抢任务
     *
     * @return: java.util.List<Task>
     */
    public List<Task> getWaittingTaskList() {
        return SqlSessionProxy.start(new SqlTask<List<Task>>() {
            @Override
            public List<Task> exec(SqlSession sqlSession) {
                // 获取映射类
                TaskDAO taskDAO = sqlSession.getMapper(TaskDAO.class);
                // 直接调用接口的方法，传入参数id=1，返回Student对象
                List<Task> taskList = taskDAO.findWaittingList();

                return taskList;
            }
        });

    }

    /**
     * 为某个容器id抢到一个任务
     *
     * @return: java.util.List<Task>
     */
    public Task grabTask(final String clientId) {

        return SqlSessionProxy.start(new SqlTask<Task>() {
            @Override
            public Task exec(SqlSession sqlSession) {
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
        });
    }

    /**
     * 任务训练状态更新为完成
     *
     * @return: java.util.List<Task>
     */
    public Boolean setTaskFinished(final String taskId) {

        return SqlSessionProxy.start(new SqlTask<Boolean>() {
            @Override
            public Boolean exec(SqlSession sqlSession) {
                // 获取映射类
                TaskDAO taskDAO = sqlSession.getMapper(TaskDAO.class);
                taskDAO.setTaskStatus(taskId, "FINISHED");

                return true;
            }
        });
    }

    /**
     * 根据token查询数据库有无该设备
     *
     * @param token
     * @return: java.lang.String
     */
    public String getClientIdByToken(final String token) {

        return SqlSessionProxy.start(new SqlTask<String>() {
            @Override
            public String exec(SqlSession sqlSession) {
                DeviceDAO deviceDAO = sqlSession.getMapper(DeviceDAO.class);
                Device device = deviceDAO.getDeviceByToken(token);
                if (device == null) {
                    return null;
                } else {
                    return device.getId();
                }
            }
        });

    }

    /**
     * 更新容器detail
     * @return
     */
    public Boolean updateDeviceGpuDetailById(final String clientId, final String detail) {

//        DeviceDAO deviceDAO = SqlSessionManager.getInstance().getMapper(DeviceDAO.class);
//        deviceDAO.updateDetailById(clientId, detail);

        return SqlSessionProxy.start(new SqlTask<Boolean>() {
            @Override
            public Boolean exec(SqlSession sqlSession) {
                // 获取映射类
                DeviceDAO deviceDAO = sqlSession.getMapper(DeviceDAO.class);
                deviceDAO.updateDetailById(clientId,detail);

                return true;
            }
        });
    }

//    private void execThreadLocalSession(){
//        try {
//            //绑定Session到线程本地变量，以后每次会自动打开关闭sqlSession
//            sqlSessionManager.startManagedSession();
//
//            // 获取映射类
//            TaskDAO taskDAO = sqlSessionManager.getMapper(TaskDAO.class);
//            // 直接调用接口的方法，传入参数id=1，返回Student对象
//
//
//            sqlSessionManager.commit();
//            return codePath;
//        } catch (Throwable t) {
//            sqlSessionManager.rollback();
//            ExceptionUtils.handle(ExceptionUtil.unwrapThrowable(t));
//            return null;
//        } finally {
//            sqlSessionManager.close();
//        }
//    }
}
