package org.zju.vipa.aix.container.center.db.service.aix;

import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Date: 2021/1/24 12:32
 * @Author: EricMa
 * @Description:
 */
public class AixDbServiceProxy implements InvocationHandler {

    private AixDbServiceImpl aixDbServiceImpl;

//    public AixDbServiceProxy(AixDbServiceImpl aixDbService) {
//        this.aixDbService = aixDbService;
//    }

    public AixDbServiceProxy() {
        aixDbServiceImpl =new AixDbServiceImpl();
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
//        if ("getSession".equals(method.getName()) || "closeSession".equals(method.getName())) {
//            return method.invoke(aixDbServiceImpl, args);
//        }
//        final DbService service = (DbService) proxy;
//        proxy instanceof DbService == true

        /** sqlSession和method的session应该是同一个 */
        SqlSession sqlSession = aixDbServiceImpl.getSession();
        try {
            Object ret = method.invoke(aixDbServiceImpl, args);
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
            aixDbServiceImpl.closeSession();
        }


    }
}
