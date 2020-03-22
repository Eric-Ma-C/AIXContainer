package zju.vipa.aix.container.center.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;
import zju.vipa.aix.container.center.db.dao.ModelDAO;
import zju.vipa.aix.container.center.db.dao.TaskDAO;
import zju.vipa.aix.container.center.db.dao.DataturksUserDAO;
import zju.vipa.aix.container.center.db.entity.Model;
import zju.vipa.aix.container.center.db.entity.Task;
import zju.vipa.aix.container.center.db.entity.DataturksUser;
import zju.vipa.aix.container.utils.ExceptionUtils;

import java.io.Reader;
import java.util.List;

/**
 * @Date: 2020/1/9 21:07
 * @Author: EricMa
 * @Description: 数据库管理器, 单例
 */
public class DbManager {

//    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionManager sqlSessionManager;

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
        Reader reader = null;
        try {
            // 加载配置文件
            reader = Resources.getResourceAsReader("mybatis/mybatis-conf.xml");
            // 构建SqlSession工厂，并从工厂里打开一个SqlSession
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
//            sqlSession = sqlSessionFactory.openSession();


            //todo 改为SqlSessionManager，看好不好用
            sqlSessionManager =SqlSessionManager.newInstance(reader);

            // 如果配置文件中没有注册接口，可以在代码里注册
            sqlSessionManager.getConfiguration().addMapper(DataturksUserDAO.class);
            sqlSessionManager.getConfiguration().addMapper(TaskDAO.class);

        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }

    }

    private SqlSession getSession(){
        SqlSession session=threadLocal.get();//得到线程当前的SqlSession
        //可以保证每个线程对应一个数据对象，在任何时刻都操作的是这个对象。
        if(sqlSessionManager.isManagedSessionStarted()){
            sqlSessionManager.startManagedSession(true);
        }
        return session;
    }


    public List<DataturksUser> getDataturksUserList() {

        // 获取映射类
        DataturksUserDAO dataturksUserDao = sqlSessionManager.getMapper(DataturksUserDAO.class);
        // 直接调用接口的方法，传入参数id=1，返回Student对象
        List<DataturksUser> dataturksUserList = dataturksUserDao.getUserList();

        //sqlSession.close();

        return dataturksUserList;
    }

    /**
     * 获取所有任务
     *
     * @param
     * @return: java.util.List<zju.vipa.aix.container.center.db.entity.Task>列表
     */
    public List<Task> getTaskList() {
        try {
            //绑定Session到线程本地变量，以后每次会自动打开关闭sqlSession
            sqlSessionManager.startManagedSession();

            // 获取映射类
            TaskDAO taskDAO = sqlSessionManager.getMapper(TaskDAO.class);
            // 直接调用接口的方法，传入参数id=1，返回Student对象
            List<Task> taskList = taskDAO.findAllList();

            sqlSessionManager.commit();
            return taskList;
        } catch (Throwable t) {
            sqlSessionManager.rollback();
            ExceptionUtils.handle(ExceptionUtil.unwrapThrowable(t));
            return null;
        } finally {
            sqlSessionManager.close();
        }




    }

    /**
     * 获取等待状态的任务列表，抢任务
     *
     * @return: java.util.List<zju.vipa.aix.container.center.db.entity.Task>
     */
    public List<Task> getWaittingTaskList() {
        try {
            //绑定Session到线程本地变量，以后每次会自动打开关闭sqlSession
            sqlSessionManager.startManagedSession();

            // 获取映射类
            TaskDAO taskDAO = sqlSessionManager.getMapper(TaskDAO.class);
            // 直接调用接口的方法，传入参数id=1，返回Student对象
            List<Task> taskList = taskDAO.findWaittingList();

            sqlSessionManager.commit();
            return taskList;
        } catch (Throwable t) {
            sqlSessionManager.rollback();
            ExceptionUtils.handle(ExceptionUtil.unwrapThrowable(t));
            return null;
        } finally {
            sqlSessionManager.close();
        }


    }

    /**
     *  为某个容器id抢到一个任务
     *
     * @return: java.util.List<zju.vipa.aix.container.center.db.entity.Task>
     */
    public String grabTask(String clientId) {

        try {
            //绑定Session到线程本地变量，以后每次会自动打开关闭sqlSession
            sqlSessionManager.startManagedSession();

            // 获取映射类
            TaskDAO taskDAO = sqlSessionManager.getMapper(TaskDAO.class);
            // 直接调用接口的方法，传入参数id=1，返回Student对象
            List<Task> taskList = taskDAO.findWaittingList();
            if(taskList==null){
                /** No waiting task,grab failed */
                return null;
            }

            Task task=taskList.get(0);
            taskDAO.updateTask(task.getId(),"TRAINING",clientId);
            // 获取映射类
            ModelDAO modelDAO = sqlSessionManager.getMapper(ModelDAO.class);
            Model model = modelDAO.findModelById(task.getModelId());
            String codePath=model.getCodePath();

            sqlSessionManager.commit();
            return codePath;
        } catch (Throwable t) {
            sqlSessionManager.rollback();
            ExceptionUtils.handle(ExceptionUtil.unwrapThrowable(t));
            return null;
        } finally {
            sqlSessionManager.close();
        }
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
