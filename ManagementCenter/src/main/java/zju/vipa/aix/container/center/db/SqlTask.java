package zju.vipa.aix.container.center.db;

import org.apache.ibatis.session.SqlSession;

/**
 * @Date: 2020/3/24 17:43
 * @Author: EricMa
 * @Description: sql执行体
 */
public interface SqlTask<T> {

    /**
     *   一次sql会话
     * @param
     * @return: T
     */
     T exec(SqlSession sqlSession);
}
