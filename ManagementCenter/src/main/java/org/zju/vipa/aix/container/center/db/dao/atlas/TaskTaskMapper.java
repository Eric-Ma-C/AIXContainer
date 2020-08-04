package org.zju.vipa.aix.container.center.db.dao.atlas;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.zju.vipa.aix.container.center.db.entity.atlas.TaskTask;

import java.util.List;

public interface TaskTaskMapper {

	/**
	 * insert task_task
	 * @param task_task
	 */
	public void insert_task_task(TaskTask task_task);

	/**
	 * update task_task
	 * @param task_task
	 */
	public void update_task_task(TaskTask task_task);

	/**
	 * delete
	 * @param id
	 */
	@Delete("delete from  task_task where id = #{id}")
	public void deleteById(int id);

	/**
	 * select
	 * @param id
	 * @return
	 */
	@Select("select t.* from task_task t where t.id = #{id}")
	public TaskTask selectById(int id);

	/**
	 * select list
	 * @param task_task
	 * @return
	 */
	public List<TaskTask> select_task_task(TaskTask task_task);

}
