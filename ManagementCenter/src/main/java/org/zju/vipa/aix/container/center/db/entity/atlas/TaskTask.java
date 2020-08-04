package org.zju.vipa.aix.container.center.db.entity.atlas;
import java.io.Serializable;

public class TaskTask implements Serializable{

  private int id;
  private String taskUid;
  private int distributed;
  private String taskType;
  private String task;
  private String result;
  private java.util.Date createdTime;
  private java.util.Date startedTime;
  private java.util.Date completedTime;
  private String note;
  private int serverId;
  private int userId;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getTaskUid() {
    return taskUid;
  }

  public void setTaskUid(String taskUid) {
    this.taskUid = taskUid;
  }


  public int getDistributed() {
    return distributed;
  }

  public void setDistributed(int distributed) {
    this.distributed = distributed;
  }


  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }


  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }


  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }


  public java.util.Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(java.util.Date createdTime) {
    this.createdTime = createdTime;
  }


  public java.util.Date getStartedTime() {
    return startedTime;
  }

  public void setStartedTime(java.util.Date startedTime) {
    this.startedTime = startedTime;
  }


  public java.util.Date getCompletedTime() {
    return completedTime;
  }

  public void setCompletedTime(java.util.Date completedTime) {
    this.completedTime = completedTime;
  }


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }


  public int getServerId() {
    return serverId;
  }

  public void setServerId(int serverId) {
    this.serverId = serverId;
  }


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

}
