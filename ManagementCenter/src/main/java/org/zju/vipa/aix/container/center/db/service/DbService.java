package org.zju.vipa.aix.container.center.db.service;

import org.apache.ibatis.session.SqlSession;
import org.zju.vipa.aix.container.center.db.entity.DataturksUser;
import org.zju.vipa.aix.container.common.entity.Task;

import java.util.List;

/**
 * @Date: 2020/7/31 10:26
 * @Author: EricMa
 * @Description: 数据库统一接口
 */
public interface DbService {

    /**
     * localSqlSession获取与直接sqlSessionFactory.openSession();相比，好在sqlSession关闭前，
     * 如果不小心在其他线程调用sqlSession的方法不会产生并发冲突
     *
     * @param
     * @return: org.apache.ibatis.session.SqlSession
     */
     SqlSession getSession() ;

    /**
     * 关闭Session
     */
    void closeSession() ;
    /**
     * 更新容器detail
     * @return
     */
    Boolean updateDeviceGpuDetailById(final String clientId, final String detail);
    /**
     * 任务训练状态更新为完成
     *
     */
    Boolean setTaskFinished(final String taskId);
    /**
     * 为某个容器id抢到一个任务
     *
     * @return: java.util.List<Task>
     */
    Task grabTask(final String clientId);
    /**
     * 根据token查询数据库有无该设备
     *
     * @param token
     * @return: java.lang.String
     */
     String getClientIdByToken(final String token);
    /**
     * 获取等待状态的任务列表，抢任务
     *
     * @return: java.util.List<Task>
     */
     List<Task> getWaittingTaskList();
    /**
     * 获取所有任务
     *
     * @param
     * @return: java.util.List<Task>列表
     */
    public List<Task> getTaskList();

    List<DataturksUser> getDataturksUserList();

    /**
     * 登录时更新容器last_login
     *
     * @return
     */
    void updateDeviceLastLoginById(String clientId);
    /**
     * 任务训练状态更新为失败
     *
     * @return: java.util.List<Task>
     */
    Boolean setTaskFailed(String taskId);
}
