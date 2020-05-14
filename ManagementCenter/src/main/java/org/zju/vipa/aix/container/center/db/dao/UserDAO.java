package org.zju.vipa.aix.container.center.db.dao;

import org.apache.ibatis.annotations.Select;
import org.zju.vipa.aix.container.center.db.entity.User;

/**
 * @Date: 2020/3/23 20:13
 * @Author: EricMa
 * @Description:
 */
public interface UserDAO {
    @Select("select * from d_users")
    public User getUserByToken();
}
