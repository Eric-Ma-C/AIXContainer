package org.zju.vipa.aix.container.center.db.service;

import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Date: 2020/7/31 10:32
 * @Author: EricMa
 * @Description: 代理实现自动事务管理，回滚等
 */
public class DbServiceProxy implements InvocationHandler {

    private DbService dbService;

    public DbServiceProxy(DbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        if("getSession".equals(method.getName())||"closeSession".equals(method.getName())){
            return method.invoke(dbService, args);
        }
//        final DbService service = (DbService) proxy;
//        proxy instanceof DbService == true

        SqlSession sqlSession = dbService.getSession();
        try {
            Object ret = method.invoke(dbService, args);
            sqlSession.commit();
            return ret;
        } catch (Throwable t) {
            sqlSession.rollback();
            ExceptionUtils.handle(ExceptionUtil.unwrapThrowable(t));

            return null;
        } finally {
            dbService.closeSession();
        }




    }
}
