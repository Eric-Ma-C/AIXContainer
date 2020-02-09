package zju.vipa.container.center.db.dao;

import org.apache.ibatis.annotations.Select;
import zju.vipa.container.center.db.entity.User;

import java.util.List;

/**
 * @Date: 2020/1/9 20:53
 * @Author: EricMa
 * @Description: todo:
 */
public interface UserDao {
        @Select("select * from d_users")
        public List<User> getUserList();
}
