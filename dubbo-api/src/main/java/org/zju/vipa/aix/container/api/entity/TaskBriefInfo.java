package org.zju.vipa.aix.container.api.entity;

import org.zju.vipa.aix.container.common.db.entity.aix.Task;

import java.io.Serializable;
import java.util.Date;

/**
 * @Date: 2020/6/5 15:00
 * @Author: EricMa
 * @Description: 简洁Task
 */
public class TaskBriefInfo implements Serializable {

    /**任务名**/
    private String name;

    /**任务类型**/
    private String type;

    /**是否为公开任务**/
    private String accessType;

    /**任务的备注信息，可以用于指明任务要求**/
    private String info;

    /**更新时间**/
    private java.util.Date updatedTime;

    /**创建时间**/
    private java.util.Date createdTime;

    /**任务状态**/
    private String status;

    public TaskBriefInfo() {
    }

    public TaskBriefInfo(Task task) {
        this.name = task.getName();
        this.type = task.getType();
        this.accessType = task.getAccessType();
        this.info = task.getInfo();
        this.updatedTime = task.getUpdatedTime();
        this.createdTime = task.getCreatedTime();
        this.status = task.getStatus();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
