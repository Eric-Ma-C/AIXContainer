package org.zju.vipa.aix.container.center.db;

import org.zju.vipa.aix.container.center.db.service.aix.AixDbService;
import org.zju.vipa.aix.container.center.db.service.aix.AixDbServiceProxy;
import org.zju.vipa.aix.container.common.db.entity.aix.FinishedTask;
import org.zju.vipa.aix.container.common.db.entity.aix.KnownError;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @Date: 2021/1/22 14:06
 * @Author: EricMa
 * @Description: 管理aix数据库
 */
public class AixDbManager implements Serializable {

    private AixDbService aixDbService;

    private static class AixDbManagerHolder {
        private static final AixDbManager INSTANCE = new AixDbManager();
    }

    private AixDbManager() {
        if (AixDbManagerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }

        init();
    }

    public static AixDbManager getInstance() {
        return AixDbManagerHolder.INSTANCE;
    }


    /**
     * 初始化
     */
    private void init() {
        /** AIX数据库接口 */
        //1.创建委托对象(AixDbServiceImpl)
//        AixDbServiceImpl aixDbService = new AixDbServiceImpl();
        //2.传入委托对象，创建调用处理器对象(InvocationHandler)
        AixDbServiceProxy aixDbServiceProxy = new AixDbServiceProxy();
        //3.传入委托对象接口和调用处理器，动态生成代理对象
        this.aixDbService = (AixDbService) Proxy.newProxyInstance(AixDbService.class.getClassLoader(), new Class[]{AixDbService.class}, aixDbServiceProxy);
        //4.通过代理对象调用方法
        //dbService.getClientIdByToken();
    }

    /**
     * 分页查询task
     *
     * @return:
     */
    public List<FinishedTask> getFinishedTaskListByPage(final int page, final int countPerPage) {

        return aixDbService.getFinishedTaskListByPage(page, countPerPage);

    }

    public void insertFinishedTask(FinishedTask finishedTask) {
        aixDbService.insertFinishedTask(finishedTask);
    }

    public List<KnownError> getKnownErrorList(){
        return aixDbService.getKnownErrorList();
    }

}

