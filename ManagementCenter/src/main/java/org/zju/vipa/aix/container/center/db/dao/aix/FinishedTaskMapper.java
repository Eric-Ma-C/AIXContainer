package org.zju.vipa.aix.container.center.db.dao.aix;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.zju.vipa.aix.container.common.db.entity.aix.FinishedTask;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @Date: 2021/1/24 12:42
 * @Author: EricMa
 * @Description:
 */
public interface FinishedTaskMapper extends Mapper<FinishedTask> {

    /**
     * select list
     *
     * @return
     */
    @Select("select * from finished_task")
    List<FinishedTask> selectByPage(RowBounds rowBounds);
}
