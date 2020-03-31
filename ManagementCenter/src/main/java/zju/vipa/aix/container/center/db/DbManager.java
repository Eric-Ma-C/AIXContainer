package zju.vipa.aix.container.center.db;

import org.apache.ibatis.session.SqlSession;
import zju.vipa.aix.container.center.db.dao.*;
import zju.vipa.aix.container.center.db.entity.Device;
import zju.vipa.aix.container.center.db.entity.Model;
import zju.vipa.aix.container.center.db.entity.Task;
import zju.vipa.aix.container.center.db.entity.DataturksUser;

import java.util.List;

/**
 * @Date: 2020/1/9 21:07
 * @Author: EricMa
 * @Description: 数据库管理器, 单例
 */
public class DbManager {

//    private SqlSessionFactory sqlSessionFactory;
//    private SqlSessionManager sqlSessionManager;


    private static class DbManagerHolder {
        private static final DbManager INSTANCE = new DbManager();
    }

    private DbManager() {
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
    }

    /**
     * 获取所有任务
     *
     * @param
     * @return: java.util.List<zju.vipa.aix.container.center.db.entity.Task>列表
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
     * @return: java.util.List<zju.vipa.aix.container.center.db.entity.Task>
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
     * @return: java.util.List<zju.vipa.aix.container.center.db.entity.Task>
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
                //TODO 方便测试用
                taskDAO.updateTask(task.getId(), "WAITING", clientId);
//                taskDAO.updateTask(task.getId(), "TRAINING", clientId);
                // 获取映射类
                ModelDAO modelDAO = sqlSession.getMapper(ModelDAO.class);
                Model model = modelDAO.findModelById(task.getModelId());
                String codePath = model.getCodePath();
                task.setCodePath(codePath);

                return task;
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
