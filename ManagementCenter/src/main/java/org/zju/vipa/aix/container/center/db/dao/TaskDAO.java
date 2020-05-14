package org.zju.vipa.aix.container.center.db.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.zju.vipa.aix.container.center.db.entity.Task;
import java.util.List;

/**
 * @Date: 2020/3/19 17:15
 * @Author: EricMa
 * @Description:
 */
public interface TaskDAO {

    int add(Task task);

//    @Update("update task")
    int update(Task task);

    @Update("update task set status=#{status} , trainBy=#{trainBy} where id=#{id}")
    public int updateTask(@Param("id")String id,@Param("status")String status, @Param("trainBy")String trainBy);


    int delete(int id);

    Task findById(int id);

    @Select("select * from task")
    List<Task> findAllList();

    @Select("select * from task where status = 'WAITING' limit 5")
    List<Task> findWaittingList();

}