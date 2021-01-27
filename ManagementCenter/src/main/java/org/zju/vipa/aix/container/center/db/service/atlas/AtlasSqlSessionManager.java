package org.zju.vipa.aix.container.center.db.service.atlas;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.zju.vipa.aix.container.center.db.SqlSessionManager;
import org.zju.vipa.aix.container.center.db.dao.*;
import org.zju.vipa.aix.container.center.db.dao.atlas.AixDeviceMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.CodesMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.ModelsMapper;
import org.zju.vipa.aix.container.center.db.dao.atlas.TaskTaskMapper;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.io.Reader;

/**
 * @Date: 2020/7/31 14:28
 * @Author: EricMa
 * @Description: 初始化sqlSession
 */
public class AtlasSqlSessionManager extends SqlSessionManager {

    public AtlasSqlSessionManager() {
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

        initMapper();
    }

    public void initMapper() {
        MapperHelper mapperHelper = new MapperHelper();
        //特殊配置
        Config config = new Config();
        //字段与变量名相同,默认驼峰转下划线
//        config.setStyle(Style.normal);
        // 主键自增回写方法,默认值MYSQL,详细说明请看文档
//        config.setIDENTITY("MYSQL");
        //设置配置
        mapperHelper.setConfig(config);
        // 注册自己项目中使用的通用Mapper接口，这里没有默认值，必须手动注册
//        mapperHelper.registerMapper(KnownErrorMapper.class);
//        mapperHelper.registerMapper(AixDeviceMapper.class);
        //配置完成后，执行下面的操作
        mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());

    }

//    public <T> T getMapper(Class<? extends Mapper> clazz) {
//        Mapper mapper = getSession().getMapper(clazz);
//        return (T) mapper;
//    }

    public <T> T getMapper(Class<T> clazz) {
        T mapper = getSession().getMapper(clazz);
        return (T) mapper;
    }


}
