package org.zju.vipa.aix.container.center.db.dao.atlas;


import org.apache.ibatis.annotations.Select;
import org.zju.vipa.aix.container.common.db.entity.atlas.Codes;

public interface CodesMapper {

	@Select("select * from codes where id = #{id}")
	public Codes findCodesById(int id);
}
