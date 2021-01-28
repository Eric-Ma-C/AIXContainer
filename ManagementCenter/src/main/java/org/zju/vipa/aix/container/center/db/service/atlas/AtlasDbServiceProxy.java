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

    private AtlasDbServiceImpl atlasDbServiceImpl;
//    private AtlasSqlSessionManager atlasSqlSessionManager;

    public AtlasDbServiceProxy() {
        atlasDbServiceImpl=new AtlasDbServiceImpl();
//        atlasSqlSessionManager=new AtlasSqlSessionManager();
    }
//    public AtlasDbServiceProxy(AtlasDbServiceImpl atlasDbServiceImpl) {
//        this.atlasDbServiceImpl = atlasDbServiceImpl;
//    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) {
//        if ("getSession".equals(method.getName()) || "closeSession".equals(method.getName())) {
//            return method.invoke(atlasDbService, args);
//        }
//        final AtlasDbService service = (AtlasDbService) proxy;
//        proxy instanceof AtlasDbService == true

        /** sqlSession和method的session应该是同一个 */
        SqlSession sqlSession =  atlasDbServiceImpl.getSession();

        /** 剩下都是sql事务方法 */
        try {
            Object ret = method.invoke(atlasDbServiceImpl, args);
            sqlSession.commit();
            return ret;
        } catch (Throwable t) {
            sqlSession.rollback();
            ExceptionUtils.handle(ExceptionUtil.unwrapThrowable(t));
//            t.printStackTrace();
//            Class<?> returnType = method.getReturnType();
//            return returnType.newInstance();
            return null;
        } finally {
            atlasDbServiceImpl.closeSession();
        }


    }
}
