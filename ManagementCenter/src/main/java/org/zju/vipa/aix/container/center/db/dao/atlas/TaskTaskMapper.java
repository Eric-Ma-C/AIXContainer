package org.zju.vipa.aix.container.center.db.dao.atlas;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.zju.vipa.aix.container.common.db.entity.atlas.TaskTask;

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

	@Update("update task_task set status='TRAINING' , train_by_id=#{trainById} where id=#{id} AND status='WAITING'")
	int taskTobeTrained(@Param("id") int id, @Param("trainById") int trainById);

	@Update("update task_task set status=#{status} where id=#{id}")
	int setTaskStatus(@Param("id") int id, @Param("status") String status);

	@Select("select * from task_task where processor = 'AIX'")
	List<TaskTask> findAllList();

	@Select("select * from task_task where status = 'WAITING' and processor = 'AIX' limit 5")
	List<TaskTask> findWaittingList();

}
