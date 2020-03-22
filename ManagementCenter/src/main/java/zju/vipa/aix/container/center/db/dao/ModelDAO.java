package zju.vipa.aix.container.center.db.dao;

import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.Mapper;
import zju.vipa.aix.container.center.db.entity.Model;

import java.util.List;

/**
 * @Date: 2020/3/20 9:36
 * @Author: EricMa
 * @Description: 神经网络模型
 */
//@Mapper
public interface ModelDAO {

    @Select("select * from model where id = #{id}")
    public Model findModelById(String id);

//    @Select("select * from user where name = #{name}")
//    public User find(String name);
//
//    @Select("select * from user where name = #{name} and pwd = #{pwd}")
//    /**
//     * 对于多个参数来说，每个参数之前都要加上@Param注解，
//     * 要不然会找不到对应的参数进而报错
//     */
//    public User login(@Param("name")String name, @Param("pwd")String pwd);
}
