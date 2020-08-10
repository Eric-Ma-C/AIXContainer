package org.zju.vipa.aix.container.common.entity;
import java.io.Serializable;

public class TaskTask implements Serializable{

  private int id;
  private String task_uid;
  private int distributed;
  private String task_type;
  private String task;
  private String result;
  private java.util.Date created_time;
  private java.util.Date started_time;
  private java.util.Date completed_time;
  private String note;
  private int server_id;
  private int user_id;
  private String access_type;
  private String info;
  private String name;
  private String processor;
  private String status;
  private int train_by_id;
  private String train_args;
  private int model_id;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getTask_uid() {
    return task_uid;
  }

  public void setTask_uid(String task_uid) {
    this.task_uid = task_uid;
  }


  public int getDistributed() {
    return distributed;
  }

  public void setDistributed(int distributed) {
    this.distributed = distributed;
  }


  public String getTask_type() {
    return task_type;
  }

  public void setTask_type(String task_type) {
    this.task_type = task_type;
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


  public java.util.Date getCreated_time() {
    return created_time;
  }

  public void setCreated_time(java.util.Date created_time) {
    this.created_time = created_time;
  }


  public java.util.Date getStarted_time() {
    return started_time;
  }

  public void setStarted_time(java.util.Date started_time) {
    this.started_time = started_time;
  }


  public java.util.Date getCompleted_time() {
    return completed_time;
  }

  public void setCompleted_time(java.util.Date completed_time) {
    this.completed_time = completed_time;
  }


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }


  public int getServer_id() {
    return server_id;
  }

  public void setServer_id(int server_id) {
    this.server_id = server_id;
  }


  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }


  public String getAccess_type() {
    return access_type;
  }

  public void setAccess_type(String access_type) {
    this.access_type = access_type;
  }


  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getProcessor() {
    return processor;
  }

  public void setProcessor(String processor) {
    this.processor = processor;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public int getTrain_by_id() {
    return train_by_id;
  }

  public void setTrain_by_id(int train_by_id) {
    this.train_by_id = train_by_id;
  }


  public String getTrain_args() {
    return train_args;
  }

  public void setTrain_args(String train_args) {
    this.train_args = train_args;
  }


  public int getModel_id() {
    return model_id;
  }

  public void setModel_id(int model_id) {
    this.model_id = model_id;
  }

}
