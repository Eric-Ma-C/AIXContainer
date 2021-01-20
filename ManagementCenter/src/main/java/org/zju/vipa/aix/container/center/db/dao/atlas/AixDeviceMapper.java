package org.zju.vipa.aix.container.center.db.dao.atlas;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;

import java.util.List;

public interface AixDeviceMapper {

    @Select("select * from aix_device where token = #{token}")
    AixDevice getDeviceByToken(String token);

    /**
     * 更新详情
     *
     * @param id
     * @param detail
     * @return: int
     */
    @Update("update aix_device set detail=#{detail} where id=#{id}")
    int updateDetailById(@Param("id") int id, @Param("detail") String detail);

    /**
     * 更新token
     *
     * @param id
     * @param token
     * @return: int
     */
    @Update("update aix_device set token=#{token} where id=#{id}")
    int updateTokenById(@Param("id") int id, @Param("token") String token);

    /**
     * 更新name
     *
     * @param id
     * @param name
     * @return: int
     */
    @Update("update aix_device set device_name=#{name} where id=#{id}")
    int updateNameById(@Param("id") int id, @Param("name") String name);

    /**
     * 更新info
     *
     * @param id
     * @param info
     * @return: int
     */
    @Update("update aix_device set info=#{info} where id=#{id}")
    int updateInfoById(@Param("id") int id, @Param("info") String info);


    /**
     * insert aix_device
     *
     * @param
     */
    @Insert("insert into aix_device(device_name,created_time,last_login,detail,info,token,user_id) values(#{device_name},NOW(),NOW(),'',#{info},#{token},#{user_id}) ")
    void insert_aix_device(@Param("device_name") String device_name, @Param("info") String info, @Param("token") String token, @Param("user_id") int user_id);

    /**
     * update aix_device
     *
     * @param aix_device
     */
    void update_aix_device(AixDevice aix_device);

    /**
     * delete
     *
     * @param id
     */
    @Delete("delete from  aix_device where id = #{id}")
    void deleteById(int id);

    /**
     * select
     *
     * @param id
     * @return
     */
    @Select("select * from aix_device where id = #{id}")
    AixDevice selectById(int id);

    /**
     * select list
     *
     * @return
     */
    @Select("select * from aix_device")
    List<AixDevice> selectByPage(RowBounds rowBounds);

    /**
     * 行数
     *
     * @return
     */
    @Select("select COUNT(*) from aix_device")
    int selectCount();

    @Update("update aix_device set last_login=NOW() where id=#{id}")
    void updateLastLoginById(int id);
}
