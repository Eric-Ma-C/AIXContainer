package org.zju.vipa.aix.container.center.db.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;

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

    //TODO 方便测试用
//    @Update("update task set status='WAITING' , trainBy=#{trainBy} where id=#{id}")
    @Update("update task set status='TRAINING' , trainBy=#{trainBy} where id=#{id}")
    int taskTobeTrained(@Param("id") String id,  @Param("trainBy") String trainBy);

    @Update("update task set status=#{status} where id=#{id}")
    int setTaskStatus(@Param("id") String id, @Param("status") String status);


    int delete(int id);

    Task findById(int id);

    @Select("select * from task")
    List<Task> findAllList();

    @Select("select * from task where status = 'WAITING' limit 5")
    List<Task> findWaittingList();

}
