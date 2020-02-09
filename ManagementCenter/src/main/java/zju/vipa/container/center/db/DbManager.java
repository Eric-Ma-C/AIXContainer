package zju.vipa.container.center.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import zju.vipa.container.center.db.dao.UserDao;
import zju.vipa.container.center.db.entity.User;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * @Date: 2020/1/9 21:07
 * @Author: EricMa
 * @Description: 数据库管理器,单例
 */
public class DbManager {

        private Reader reader;
        private SqlSession sqlSession;


        private static class DbManagerHolder {
                private static final DbManager INSTANCE = new DbManager();
        }

        private DbManager() {
                init();
        }

        public static DbManager getInstance() {
                return DbManagerHolder.INSTANCE;
        }


        /**
         * 初始化
         */
        private void init() {
                try {
                        // 加载配置文件
                        reader = Resources.getResourceAsReader("mybatis/mybatis-conf.xml");
                } catch (IOException e) {
                        e.printStackTrace();
                }
                // 构建SqlSession工厂，并从工厂里打开一个SqlSession
                SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                sqlSession = sqlSessionFactory.openSession();
                // 如果配置文件中没有注册接口，可以在代码里注册
                sqlSession.getConfiguration().addMapper(UserDao.class);
        }


        public List<User> getUsers() {

                // 获取映射类
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                // 直接调用接口的方法，传入参数id=1，返回Student对象
                List<User> userList = userDao.getUserList();


                sqlSession.close();


                return userList;
        }
}
