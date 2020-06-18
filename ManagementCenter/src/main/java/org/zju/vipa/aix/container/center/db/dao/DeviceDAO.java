package org.zju.vipa.aix.container.center.db.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.zju.vipa.aix.container.center.db.entity.Device;

/**
 * @Date: 2020/3/23 20:15
 * @Author: EricMa
 * @Description: 设备表操作
 */
public interface DeviceDAO {
    @Select("select * from device where token = #{token}")
    public Device getDeviceByToken(String token);

    /**
     *  更新详情
     * @param id
     * @param detail
     * @return: int
     */
    @Update("update device set detail=#{detail} where id=#{id}")
    public int updateDetailById(@Param("id") String id, @Param("detail") String detail);



}
