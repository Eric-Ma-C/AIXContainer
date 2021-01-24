package org.zju.vipa.aix.container.center.db.service.aix;

import org.apache.ibatis.session.SqlSession;
import org.zju.vipa.aix.container.common.db.entity.aix.FinishedTask;

import java.util.List;

/**
 * @Date: 2021/1/22 15:40
 * @Author: EricMa
 * @Description:
 */
public interface AixDbService {
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
     *  分页查询执行的任务
     * @param page 查询第几页
     * @param countPerPage 每页的条数(一般固定值)
     * @return:
     */
    List<FinishedTask> getFinishedTaskListByPage(int page, int countPerPage);

    boolean insertFinishedTask(FinishedTask finishedTask);
}
