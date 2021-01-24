package org.zju.vipa.aix.container.center.db;

import org.zju.vipa.aix.container.center.db.service.atlas.AtlasDbServiceImpl;
import org.zju.vipa.aix.container.center.db.service.atlas.AtlasDbService;
import org.zju.vipa.aix.container.center.db.service.atlas.AtlasDbServiceProxy;
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
public class AtlasDbManager implements Serializable {

    private AtlasDbService atlasDbService;

    private static class AtlasDbManagerHolder {
        private static final AtlasDbManager INSTANCE = new AtlasDbManager();
    }

    private AtlasDbManager() {
        if (AtlasDbManagerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }

        init();
    }

    public static AtlasDbManager getInstance() {
        return AtlasDbManagerHolder.INSTANCE;
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
        AtlasDbServiceProxy atlasDbServiceProxy = new AtlasDbServiceProxy(atlasDbService);
        //3.传入委托对象接口和调用处理器，动态生成代理对象
        this.atlasDbService = (AtlasDbService) Proxy.newProxyInstance(AtlasDbServiceImpl.class.getClassLoader(), AtlasDbServiceImpl.class.getInterfaces(), atlasDbServiceProxy);
        //4.通过代理对象调用方法
        //dbService.getClientIdByToken();
    }


    public List<DataturksUser> getDataturksUserList() {


        return atlasDbService.getDataturksUserList();
    }

    /**
     * 获取所有任务
     *
     * @param
     * @return: java.util.List<Task>列表
     */
    public List<Task> getTaskList() {
        return atlasDbService.getTaskList();
    }

    /**
     * 获取等待状态的任务列表，抢任务
     *
     * @return: java.util.List<Task>
     */
    public List<Task> getWaittingTaskList() {
        return atlasDbService.getWaittingTaskList();

    }

    /**
     * 为某个容器id抢到一个任务
     *
     * @return: java.util.List<Task>
     */
    public Task grabTask(final String clientId) {

        return atlasDbService.grabTask(clientId);
    }

    /**
     * 任务训练状态更新为完成
     *
     * @return: java.util.List<Task>
     */
    public boolean setTaskFinished(final String taskId) {

        return atlasDbService.setTaskFinished(taskId);
    }

    /**
     * 任务训练状态更新为失败
     *
     * @return: java.util.List<Task>
     */
    public boolean setTaskFailed(final String taskId) {

        return atlasDbService.setTaskFailedById(taskId);
    }

    /**
     * 根据token查询数据库有无该设备
     *
     * @param token
     * @return: java.lang.String
     */
    public String getClientIdByToken(final String token) {


        return atlasDbService.getClientIdByToken(token);


    }

    /**
     * 根据token查询数据库有无该设备
     *
     * @param token
     * @return:
     */
    public AixDevice getClientByToken(final String token) {

        return atlasDbService.getClientByToken(token);

    }

    /**
     * 根据id找device
     *
     * @param id
     * @return:
     */
    public AixDevice getClientById(final String id) {

        return atlasDbService.getClientById(id);

    }

    /**
     * 分页查询device
     *
     * @return:
     */
    public List<AixDevice> getClientListByPage(final int page, final int countPerPage) {

        return atlasDbService.getClientListByPage(page, countPerPage);

    }

    public int getClientCount() {
        return atlasDbService.getClientCount();
    }

    /**
     * 更新容器detail
     *
     * @return
     */
    public boolean updateDeviceGpuDetailById(final String clientId, final String detail) {

        return atlasDbService.updateDeviceGpuDetailById(clientId, detail);
    }

    /**
     * 更新容器token
     *
     * @return
     */
    public boolean updateDeviceTokenById(final String clientId, final String token) {

        return atlasDbService.updateDeviceTokenById(clientId, token);
    }

    /**
     * 更新容器name
     *
     * @return
     */
    public boolean updateDeviceNameById(final String clientId, final String name) {
        Boolean ok = atlasDbService.updateDeviceNameById(clientId, name);
        return ok == null ? false : ok;
    }

    /**
     * 更新容器info
     *
     * @return
     */
    public boolean updateDeviceInfoById(final String clientId, final String info) {

        return atlasDbService.updateDeviceInfoById(clientId, info);
    }

    /**
     * 更新容器last_login
     *
     * @return
     */
    public void updateDeviceLastLoginById(final String clientId) {
        atlasDbService.updateDeviceLastLoginById(clientId);
    }

    public void setTaskWaitingById(String taskId) {
        atlasDbService.setTaskWaitingById(taskId);
    }

    public void insertClient(AixDevice aixDevice) {
        atlasDbService.insertClient(aixDevice);
    }
}
