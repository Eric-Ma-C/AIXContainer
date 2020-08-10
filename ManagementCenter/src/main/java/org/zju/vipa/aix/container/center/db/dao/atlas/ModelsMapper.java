package org.zju.vipa.aix.container.center.db.dao.atlas;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.zju.vipa.aix.container.center.db.entity.atlas.Models;

import java.util.List;

public interface ModelsMapper {

	@Select("select * from models where id = #{id}")
	public Models findModelById(int id);

	/**
	 * insert models
	 * @param models
	 */
	public void insert_models(Models models);

	/**
	 * update models
	 * @param models
	 */
	public void update_models(Models models);

	/**
	 * delete
	 * @param id
	 */
	@Delete("delete from  models where id = #{id}")
	public void deleteById(int id);

	/**
	 * select
	 * @param id
	 * @return
	 */
	@Select("select t.* from models t where t.id = #{id}")
	public Models selectById(int id);

	/**
	 * select list
	 * @param models
	 * @return
	 */
	public List<Models> select_models(Models models);

}
