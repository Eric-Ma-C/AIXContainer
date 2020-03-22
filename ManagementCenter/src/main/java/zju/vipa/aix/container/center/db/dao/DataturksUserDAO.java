package zju.vipa.aix.container.center.db.dao;

import org.apache.ibatis.annotations.Select;
import zju.vipa.aix.container.center.db.entity.DataturksUser;

import java.util.List;

/**
 * @Date: 2020/1/9 20:53
 * @Author: EricMa
 * @Description: todo: 待完善
 */
public interface DataturksUserDAO {
        @Select("select * from d_users")
        public List<DataturksUser> getUserList();
}
