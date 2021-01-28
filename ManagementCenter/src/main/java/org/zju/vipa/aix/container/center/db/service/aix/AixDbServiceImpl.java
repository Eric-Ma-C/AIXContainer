package org.zju.vipa.aix.container.center.db.service.aix;


import org.apache.ibatis.session.RowBounds;
import org.zju.vipa.aix.container.center.db.dao.aix.FinishedTaskMapper;
import org.zju.vipa.aix.container.center.db.dao.aix.KnownErrorMapper;
import org.zju.vipa.aix.container.common.db.entity.aix.FinishedTask;
import org.zju.vipa.aix.container.common.db.entity.aix.KnownError;

import java.util.List;


/**
 * @Date: 2020/7/31 10:30
 * @Author: EricMa
 * @Description: aix  db操作具体实现
 */
public class AixDbServiceImpl extends AixSqlSessionManager implements AixDbService{

//    @Override
//    public SqlSession getSession() {
//        return super.getSession();
//    }
//
//    @Override
//    public void closeSession() {
//         super.closeSession();
//    }

    @Override
    public List<FinishedTask> getFinishedTaskListByPage(int page, int countPerPage) {
        FinishedTaskMapper finishedTaskMapper = getSession().getMapper(FinishedTaskMapper.class);
        RowBounds rowBounds=new RowBounds((page-1)*countPerPage,countPerPage);
        List<FinishedTask> taskList = finishedTaskMapper.selectByPage(rowBounds);

        return taskList;
    }

    @Override
    public boolean insertFinishedTask(FinishedTask finishedTask) {
        FinishedTaskMapper deviceMapper = getMapper(FinishedTaskMapper.class);
        return deviceMapper.insert(finishedTask)>0;

    }

    @Override
    public List<KnownError> getKnownErrorList() {
        KnownErrorMapper knownErrorMapper=getMapper(KnownErrorMapper.class);

        return knownErrorMapper.selectAll();
    }


}
