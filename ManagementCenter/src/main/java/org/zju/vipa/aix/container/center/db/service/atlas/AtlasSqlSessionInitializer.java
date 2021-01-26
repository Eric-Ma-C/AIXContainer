package org.zju.vipa.aix.container.center.db.service.atlas;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.zju.vipa.aix.container.center.db.SqlSessionInitializer;
import org.zju.vipa.aix.container.center.db.dao.*;
import org.zju.vipa.aix.container.center.db.dao.atlas.AixDeviceMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.CodesMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.ModelsMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.TaskTaskMapper;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;

import java.io.Reader;

/**
 * @Date: 2020/7/31 14:28
 * @Author: EricMa
 * @Description: 初始化sqlSession
 */
public class AtlasSqlSessionInitializer extends SqlSessionInitializer {

    public AtlasSqlSessionInitializer() {
        Reader reader = null;
        try {
            // 加载配置文件
            reader = Resources.getResourceAsReader("mybatis/mybatis-atlas-conf.xml");
            // 构建SqlSession工厂，并从工厂里打开一个SqlSession
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSessionFactory.getConfiguration().addMapper(DataturksUserDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(TaskDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(UserDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(DeviceDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(ModelDAO.class);
            sqlSessionFactory.getConfiguration().addMapper(TaskTaskMapper.class);
            sqlSessionFactory.getConfiguration().addMapper(AixDeviceMapper.class);
            sqlSessionFactory.getConfiguration().addMapper(ModelsMapper.class);
            sqlSessionFactory.getConfiguration().addMapper(CodesMapper.class);


        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }
    }

}
