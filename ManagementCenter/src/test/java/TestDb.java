import zju.vipa.container.center.db.DbManager;
import zju.vipa.container.center.db.entity.User;

import java.util.List;

/**
 * @Date: 2020/1/9 21:43
 * @Author: EricMa
 * @Description: 测试
 */
public class TestDb {
        public static void main(String[] args) {
               getUserList();
        }

        private static void getUserList(){
                List<User> userList=DbManager.getInstance().getUsers();
                for (User user : userList) {
                        System.out.println(user.toString());
                }
        }
}
