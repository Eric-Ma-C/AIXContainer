package org.zju.vipa.aix.container.center.db.dao.atlas;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.zju.vipa.aix.container.center.db.entity.atlas.AixDevice;

import java.util.List;

public interface AixDeviceMapper {

	@Select("select * from device where token = #{token}")
	public AixDevice getDeviceByToken(String token);

	/**
	 *  更新详情
	 * @param id
	 * @param detail
	 * @return: int
	 */
	@Update("update device set detail=#{detail} where id=#{id}")
	public int updateDetailById(@Param("id") String id, @Param("detail") String detail);



	/**
	 * insert aix_device
	 * @param aix_device
	 */
	public void insert_aix_device(AixDevice aix_device);

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
	@Select("select t.* from aix_device t where t.id = #{id}")
	public AixDevice selectById(int id);

	/**
	 * select list
	 * @param aix_device
	 * @return
	 */
	public List<AixDevice> select_aix_device(AixDevice aix_device);

}
