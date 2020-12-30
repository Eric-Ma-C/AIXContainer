package org.zju.vipa.aix.container.center.db.dao.atlas;

import org.apache.ibatis.annotations.*;
import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;

import java.util.List;

public interface AixDeviceMapper {

	@Select("select * from aix_device where token = #{token}")
	public AixDevice getDeviceByToken(String token);

	/**
	 *  更新详情
	 * @param id
	 * @param detail
	 * @return: int
	 */
	@Update("update aix_device set detail=#{detail} where id=#{id}")
	public int updateDetailById(@Param("id") int id, @Param("detail") String detail);



	/**
	 * insert aix_device
	 * @param
	 */
	@Insert("insert into aix_device(device_name,created_time,last_login,detail,info,token,user_id) values(#{device_name},NOW(),NOW(),'',#{info},#{token},#{user_id}) ")
	public void insert_aix_device(@Param("device_name") String device_name,@Param("info") String info,@Param("token") String token,@Param("user_id") int user_id);

	/**
	 * update aix_device
	 * @param aix_device
	 */
	public void update_aix_device(AixDevice aix_device);

	/**
	 * delete
	 * @param id
	 */
	@Delete("delete from  aix_device where id = #{id}")
	public void deleteById(int id);

	/**
	 * select
	 * @param id
	 * @return
	 */
	@Select("select * from aix_device where id = #{id}")
	public AixDevice selectById(int id);

	/**
	 * select list
	 * @param aix_device
	 * @return
	 */
	public List<AixDevice> select_aix_device(AixDevice aix_device);

	@Update("update aix_device set last_login=NOW() where id=#{id}")
    void updateLastLoginById(int id);
}
