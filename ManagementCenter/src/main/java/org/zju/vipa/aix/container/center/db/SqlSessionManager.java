package org.zju.vipa.aix.container.center.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.zju.vipa.aix.container.center.db.dao.*;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;

import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Date: 2020/5/21 19:14
 * @Author: EricMa
 * @Description: todo 管理自动事务SqlSession
 */
public class SqlSessionManager {
    private static SqlSessionFactory sqlSessionFactory;
    private final SqlSession sqlSessionProxy;

    /**
     * 保证每个线程一个session
     */
    private static ThreadLocal<SqlSession> localSqlSession = new ThreadLocal<>();

    private static class SqlSessionManagerHolder {
        private static final SqlSessionManager INSTANCE = new SqlSessionManager();
    }

//    public static SqlSession getSession() {
////        return SqlSessionManagerHolder.INSTANCE.;
//    }

    private SqlSessionManager() {
        if (SqlSessionManagerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }

        sqlSessionProxy = (SqlSession) Proxy.newProxyInstance(
            SqlSessionFactory.class.getClassLoader(),
            new Class[]{SqlSession.class},
            new SqlSessionInterceptor());
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


        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }
    }

    /**
     * localSqlSession获取与直接sqlSessionFactory.openSession();相比，好在sqlSession关闭前，
     * 如果不小心在其他线程调用sqlSession的方法不会产生并发冲突
     *
     * @param
     * @return: org.apache.ibatis.session.SqlSession
     */
    private SqlSession getLocalSession() {
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
    private void closeSession() {
        //从当前线程变量获取
        SqlSession sqlSession = localSqlSession.get();
        if (sqlSession != null) {
            sqlSession.close();
            localSqlSession.remove();
        }
    }


    /**
     * 自动事务处理 回滚
     */
    private class SqlSessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SqlSession autoSqlSession = getLocalSession();
            Object obj = null;
            try {
                obj = method.invoke(autoSqlSession, args);

                autoSqlSession.commit();
            } catch (Throwable t) {
                autoSqlSession.rollback();
                throw t;

            } finally {
                closeSession();
            }
            return obj;
        }
    }
}
