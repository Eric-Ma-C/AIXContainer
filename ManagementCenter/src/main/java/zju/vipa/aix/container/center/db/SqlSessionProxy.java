package zju.vipa.aix.container.center.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import zju.vipa.aix.container.center.db.dao.*;
import zju.vipa.aix.container.center.util.ExceptionUtils;

import java.io.Reader;

/**
 * @Date: 2020/3/24 17:37
 * @Author: EricMa
 * @Description: 封装 sql session
 */
public class SqlSessionProxy {

    private static SqlSessionFactory sqlSessionFactory;
    /**
     * 保证每个线程一个session
     */
    private static ThreadLocal<SqlSession> localSqlSession = new ThreadLocal<>();

    private SqlSessionProxy() {
    }

    static {
        Reader reader = null;
        try {
            // 加载配置文件
            reader = Resources.getResourceAsReader("mybatis/mybatis-conf.xml");
            // 构建SqlSession工厂，并从工厂里打开一个SqlSession
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSessionFactory.getConfiguration().addMapper(DataturksUserDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(TaskDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(UserDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(DeviceDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(ModelDAO.class);


//            // 改为SqlSessionManager，看好不好用
//            sqlSessionManager = SqlSessionManager.newInstance(reader);
//
//            // 如果配置文件中没有注册接口，可以在代码里注册
//            sqlSessionManager.getConfiguration().addMapper(DataturksUserDAO.class);
//            sqlSessionManager.getConfiguration().addMapper(TaskDAO.class);
//            sqlSessionManager.getConfiguration().addMapper(UserDAO.class);
//            sqlSessionManager.getConfiguration().addMapper(DeviceDAO.class);
//            sqlSessionManager.getConfiguration().addMapper(ModelDAO.class);


        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }
    }

    /**
     * 与直接sqlSessionFactory.openSession();相比，好在sqlSession关闭前，
     * 如果不小心在其他线程调用sqlSession的方法不会产生并发冲突
     *
     * @param
     * @return: org.apache.ibatis.session.SqlSession
     */
    private static SqlSession getSession() {
        SqlSession session = localSqlSession.get();
        if (session == null) {
            session = sqlSessionFactory.openSession();
            localSqlSession.set(session);
        }

        return session;
    }

    /**
     * 关闭Session
     */
    private static void closeSession() {
        //从当前线程获取
        SqlSession sqlSession = localSqlSession.get();
        if (sqlSession != null) {
            sqlSession.close();
            localSqlSession.remove();
        }
    }


    /**
     * 执行自定义的sql任务，自动处理回滚
     *
     * @param task 要执行的任务
     * @return: T 外部接口的返回值
     */
    public static <T> T start(SqlTask<T> task) {
        SqlSession sqlSession = getSession();
        try {
            T obj = task.exec(sqlSession);

            sqlSession.commit();
            return obj;
        } catch (Throwable t) {
            ExceptionUtils.handle(ExceptionUtil.unwrapThrowable(t));
            sqlSession.rollback();

            return null;
        } finally {
            closeSession();
        }
    }
}
