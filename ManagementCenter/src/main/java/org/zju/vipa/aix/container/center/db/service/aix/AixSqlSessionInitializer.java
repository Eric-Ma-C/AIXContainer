package org.zju.vipa.aix.container.center.db.service.aix;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.zju.vipa.aix.container.center.db.SqlSessionInitializer;
import org.zju.vipa.aix.container.center.db.dao.aix.FinishedTaskMapper;
import org.zju.vipa.aix.container.center.db.dao.aix.KnownErrorMapper;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.io.Reader;

/**
 * @Date: 2021/1/22 14:30
 * @Author: EricMa
 * @Description:
 */
public class AixSqlSessionInitializer extends SqlSessionInitializer {


    public  AixSqlSessionInitializer(){
        Reader reader = null;
        try {
            // 加载配置文件
            reader = Resources.getResourceAsReader("mybatis/mybatis-aix-conf.xml");
            // 构建SqlSession工厂，并从工厂里打开一个SqlSession
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSessionFactory.getConfiguration().addMapper(KnownErrorMapper.class);
            sqlSessionFactory.getConfiguration().addMapper(FinishedTaskMapper.class);


        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }

        initMapper();
    }

    private void initMapper() {
        MapperHelper mapperHelper = new MapperHelper();
        //特殊配置
        Config config = new Config();
        //字段与变量名相同,默认驼峰转下划线
//        config.setStyle(Style.normal);
        //设置配置
        mapperHelper.setConfig(config);
        // 注册自己项目中使用的通用Mapper接口，这里没有默认值，必须手动注册
        mapperHelper.registerMapper(KnownErrorMapper.class);
        mapperHelper.registerMapper(FinishedTaskMapper.class);
        //配置完成后，执行下面的操作
        mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());

    }

}

