package org.zju.vipa.aix.container.center.db.service;

import org.apache.ibatis.session.SqlSession;

/**
 * @Date: 2020/7/31 14:38
 * @Author: EricMa
 * @Description:
 */
@Deprecated
public interface AutoSqlTask {
    /**
     *   一次sql会话
     * @param
     */
    Object exec(SqlSession sqlSession);
}
