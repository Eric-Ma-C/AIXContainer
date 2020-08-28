package org.zju.vipa.aix.container.center.db.dao;

import org.apache.ibatis.annotations.Select;
import org.zju.vipa.aix.container.common.db.entity.aix.DataturksUser;

import java.util.List;

/**
 * @Date: 2020/1/9 20:53
 * @Author: EricMa
 * @Description:
 */
public interface DataturksUserDAO {
        @Select("select * from d_users")
        List<DataturksUser> getUserList();
}
