package org.zju.vipa.aix.container.center.db.service.atlas;

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
public class AtlasDbServiceProxy implements InvocationHandler {

    private AtlasDbService atlasDbService;

    public AtlasDbServiceProxy(AtlasDbService atlasDbService) {
        this.atlasDbService = atlasDbService;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        if ("getSession".equals(method.getName()) || "closeSession".equals(method.getName())) {
            return method.invoke(atlasDbService, args);
        }
//        final DbService service = (DbService) proxy;
//        proxy instanceof DbService == true

        /** sqlSession和method的session应该是同一个 */
        SqlSession sqlSession = atlasDbService.getSession();
        try {
            Object ret = method.invoke(atlasDbService, args);
            sqlSession.commit();
            return ret;
        } catch (Throwable t) {
//            t.printStackTrace();
            sqlSession.rollback();
            ExceptionUtils.handle(ExceptionUtil.unwrapThrowable(t));

//            Class<?> returnType = method.getReturnType();
//            return returnType.newInstance();
            return null;
        } finally {
            atlasDbService.closeSession();
        }


    }
}
