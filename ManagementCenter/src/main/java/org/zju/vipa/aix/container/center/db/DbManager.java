package org.zju.vipa.aix.container.center.db;

import org.zju.vipa.aix.container.center.db.service.AtlasDbServiceImpl;
import org.zju.vipa.aix.container.center.db.service.DbService;
import org.zju.vipa.aix.container.center.db.service.DbServiceProxy;
import org.zju.vipa.aix.container.common.db.entity.aix.DataturksUser;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;
import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @Date: 2020/1/9 21:07
 * @Author: EricMa
 * @Description: 数据库管理器, 单例
 */
public class DbManager implements Serializable {

    //    private SqlSessionFactory sqlSessionFactory;
    private org.apache.ibatis.session.SqlSessionManager sqlSessionManager;
    private DbService dbService;

    private static class DbManagerHolder {
        private static final DbManager INSTANCE = new DbManager();
    }

    private DbManager() {
        if (DbManagerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }

        init();
    }

    public static DbManager getInstance() {
        return DbManagerHolder.INSTANCE;
    }


    /**
     * 初始化
     */
    private void init() {
        /** AIX数据库接口 */
//        //1.创建委托对象
//        AixDbServiceImpl aixDbService = new AixDbServiceImpl();
//        //2.创建调用处理器(InvocationHandler)对象
//        DbServiceProxy dbServiceProxy = new DbServiceProxy(aixDbService);
//        //3.动态生成代理对象
//        dbService = (DbService) Proxy.newProxyInstance(AixDbServiceImpl.class.getClassLoader(), AixDbServiceImpl.class.getInterfaces(), dbServiceProxy);
//        //4.通过代理对象调用方法
//        //dbService.getClientIdByToken();


        /** Atlas数据库接口 */
        //1.创建委托对象(DbService)
        AtlasDbServiceImpl atlasDbService = new AtlasDbServiceImpl();
        //2.传入委托对象，创建调用处理器对象(InvocationHandler)
        DbServiceProxy dbServiceProxy = new DbServiceProxy(atlasDbService);
        //3.传入委托对象接口和调用处理器，动态生成代理对象
        dbService = (DbService) Proxy.newProxyInstance(AtlasDbServiceImpl.class.getClassLoader(), AtlasDbServiceImpl.class.getInterfaces(), dbServiceProxy);
        //4.通过代理对象调用方法
        //dbService.getClientIdByToken();
    }


    public List<DataturksUser> getDataturksUserList() {


        return dbService.getDataturksUserList();
    }

    /**
     * 获取所有任务
     *
     * @param
     * @return: java.util.List<Task>列表
     */
    public List<Task> getTaskList() {
        return dbService.getTaskList();
    }

    /**
     * 获取等待状态的任务列表，抢任务
     *
     * @return: java.util.List<Task>
     */
    public List<Task> getWaittingTaskList() {
        return dbService.getWaittingTaskList();

    }

    /**
     * 为某个容器id抢到一个任务
     *
     * @return: java.util.List<Task>
     */
    public Task grabTask(final String clientId) {

        return dbService.grabTask(clientId);
    }

    /**
     * 任务训练状态更新为完成
     *
     * @return: java.util.List<Task>
     */
    public Boolean setTaskFinished(final String taskId) {

        return dbService.setTaskFinished(taskId);
    }
    /**
     * 任务训练状态更新为失败
     *
     * @return: java.util.List<Task>
     */
    public Boolean setTaskFailed(final String taskId) {

        return dbService.setTaskFailedById(taskId);
    }

    /**
     * 根据token查询数据库有无该设备
     *
     * @param token
     * @return: java.lang.String
     */
    public String getClientIdByToken(final String token) {


        return dbService.getClientIdByToken(token);


    }

    /**
     * 根据token查询数据库有无该设备
     *
     * @param token
     * @return:
     */
    public AixDevice getClientByToken(final String token) {

        return dbService.getClientByToken(token);

    }

    /**
     * 根据id找device
     *
     * @param id
     * @return:
     */
    public AixDevice getClientById(final String id) {

        return dbService.getClientById(id);

    }

    /**
     * 分页查询device
     *
     * @return:
     */
    public List<AixDevice> getClientListByPage(final int page,final int countPerPage) {

        return dbService.getClientListByPage(page,countPerPage);

    }

    public int getClientCount(){
        return dbService.getClientCount();
    }

    /**
     * 更新容器detail
     *
     * @return
     */
    public Boolean updateDeviceGpuDetailById(final String clientId, final String detail) {

        return dbService.updateDeviceGpuDetailById(clientId, detail);
    }

    /**
     * 更新容器token
     *
     * @return
     */
    public Boolean updateDeviceTokenById(final String clientId, final String token) {

        return dbService.updateDeviceTokenById(clientId, token);
    }

    /**
     * 更新容器name
     *
     * @return
     */
    public Boolean updateDeviceNameById(final String clientId, final String name) {

        return dbService.updateDeviceNameById(clientId, name);
    }
    /**
     * 更新容器last_login
     * @return
     */
    public void updateDeviceLastLoginById(final String clientId) {
         dbService.updateDeviceLastLoginById(clientId);
    }

    public void setTaskWaitingById(String taskId){
        dbService.setTaskWaitingById(taskId);
    }

    public void insertClient(AixDevice aixDevice){
        dbService.insertClient(aixDevice);
    }
}
