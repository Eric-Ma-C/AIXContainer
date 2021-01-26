package org.zju.vipa.aix.container.center.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zju.vipa.aix.container.center.log.LogUtils;

/**
 * @Date: 2021/1/22 15:44
 * @Author: EricMa
 * @Description:
 */
public class SqlSessionInitializer {
    protected SqlSessionFactory sqlSessionFactory;
    /**
     * 保证每个线程一个session
     */
    private ThreadLocal<SqlSession> localSqlSession = new ThreadLocal<>();


    /**
     * localSqlSession获取与直接sqlSessionFactory.openSession();相比，好在sqlSession关闭前，
     * 如果不小心在其他线程调用sqlSession的方法不会产生并发冲突
     *
     * @param
     * @return: org.apache.ibatis.session.SqlSession
     */
    protected SqlSession getSession() {
        SqlSession session = localSqlSession.get();
        if (session == null) {
            session = sqlSessionFactory.openSession();
            localSqlSession.set(session);
        }
        LogUtils.info("getSession().Configuration.getEnvironmentId={}",session);
        return session;
    }

    /**
     * 关闭Session
     */
    protected void closeSession() {
        //从当前线程变量获取
        SqlSession session = localSqlSession.get();
        if (session != null) {
            LogUtils.info("closeSession().Configuration.getEnvironment={}",session);
            session.close();
            localSqlSession.remove();
        }
    }
}
