package org.zju.vipa.aix.container.center.db.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.zju.vipa.aix.container.center.db.dao.*;
import org.zju.vipa.aix.container.center.db.dao.atlas.AixDeviceMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.ModelsMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.TaskTaskMapper;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;

import java.io.Reader;

/**
 * @Date: 2020/7/31 14:28
 * @Author: EricMa
 * @Description: 初始化sqlSession
 */
public class SqlSessionInitializer {
    private static SqlSessionFactory sqlSessionFactory;
    /**
     * 保证每个线程一个session
     */
    private static ThreadLocal<SqlSession> localSqlSession = new ThreadLocal<>();

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
            sqlSessionFactory.getConfiguration().addMapper(TaskTaskMapper.class);
            sqlSessionFactory.getConfiguration().addMapper(AixDeviceMapper.class);
            sqlSessionFactory.getConfiguration().addMapper(ModelsMapper.class);

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
    protected SqlSession getSession() {
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
    protected void closeSession() {
        //从当前线程变量获取
        SqlSession sqlSession = localSqlSession.get();
        if (sqlSession != null) {
            sqlSession.close();
            localSqlSession.remove();
        }
    }
}
