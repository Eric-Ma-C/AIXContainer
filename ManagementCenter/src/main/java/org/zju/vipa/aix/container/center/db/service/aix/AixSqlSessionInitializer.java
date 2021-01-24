package org.zju.vipa.aix.container.center.db.service.aix;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.zju.vipa.aix.container.center.db.SqlSessionInitializer;
import org.zju.vipa.aix.container.center.db.dao.aix.KnownErrorMapper;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;

import java.io.Reader;

/**
 * @Date: 2021/1/22 14:30
 * @Author: EricMa
 * @Description:
 */
public class AixSqlSessionInitializer extends SqlSessionInitializer {


    static {
        Reader reader = null;
        try {
            // 加载配置文件
            reader = Resources.getResourceAsReader("mybatis/mybatis-aix-conf.xml");
            // 构建SqlSession工厂，并从工厂里打开一个SqlSession
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSessionFactory.getConfiguration().addMapper(KnownErrorMapper.class);


        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }
    }

}

