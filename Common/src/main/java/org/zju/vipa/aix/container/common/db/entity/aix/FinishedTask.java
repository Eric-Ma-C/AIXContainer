package org.zju.vipa.aix.container.common.db.entity.aix;

import org.zju.vipa.aix.container.common.utils.TimeUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FinishedTask implements Serializable{
  @Id
  @GeneratedValue(generator = "JDBC")
  private int id;

  /**执行该任务的设备id**/
  private int device_id;

  /**执行任务的id,一个任务可能被多次执行**/
  private String task_id;

  /**任务状态(SUCCESS,FAILED,ENV_FAILED,CANCEL)**/
  private String status;

  /**开始时间**/
  private java.util.Date begin;

  /**结束时间**/
  private java.util.Date end;

  /**持续时长,单位分钟**/
  private int duration_min;

  /**任务日志,json数组**/
  private String logs;

  public FinishedTask() {
  }

  public FinishedTask(String device_id, String task_id, String status, Date begin, Date end, List<String> logs) {
    this.device_id = Integer.parseInt(device_id);
    this.task_id = task_id;
    this.status = status;
    this.begin = begin;
    this.end = end;
    this.duration_min = TimeUtils.dateMinusMin(end,begin);
    StringBuilder sb=new StringBuilder();
    for (String log : logs) {
      sb.append(log);
    }
    this.logs=sb.toString();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public int getDevice_id() {
    return device_id;
  }

  public void setDevice_id(int device_id) {
    this.device_id = device_id;
  }


  public String getTask_id() {
    return task_id;
  }

  public void setTask_id(String task_id) {
    this.task_id = task_id;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public java.util.Date getBegin() {
    return begin;
  }

  public void setBegin(java.util.Date begin) {
    this.begin = begin;
  }


  public java.util.Date getEnd() {
    return end;
  }

  public void setEnd(java.util.Date end) {
    this.end = end;
  }


  public int getDuration_min() {
    return duration_min;
  }

  public void setDuration_min(int duration_min) {
    this.duration_min = duration_min;
  }


  public String getLogs() {
    return logs;
  }

  public void setLogs(String logs) {
    this.logs = logs;
  }

}
