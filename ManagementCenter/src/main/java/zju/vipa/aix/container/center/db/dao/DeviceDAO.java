package zju.vipa.aix.container.center.db.dao;

import org.apache.ibatis.annotations.Select;
import zju.vipa.aix.container.center.db.entity.Device;

/**
 * @Date: 2020/3/23 20:15
 * @Author: EricMa
 * @Description: 设备表操作
 */
public interface DeviceDAO {
    @Select("select * from device where token = #{token}")
    public Device getDeviceByToken(String token);
}
