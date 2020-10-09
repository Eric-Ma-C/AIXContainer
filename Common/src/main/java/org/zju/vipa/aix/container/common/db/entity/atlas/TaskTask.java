package org.zju.vipa.aix.container.common.db.entity.atlas;
import java.io.Serializable;

public class TaskTask implements Serializable{

  private int id;
  private String task_uid;
  private int distributed;
  private String task;
  private String result;
  private java.util.Date created_time;
  private java.util.Date started_time;
  private java.util.Date completed_time;
  private String note;
  private int server_id;
  private int user_id;
  private String info;
  private String name;
  private String processor;
  private String status;
  private int train_by_id;
  private int flowchart_id;
  private String node_uid;
  private int is_public;
  private String stage_type;
  private int multiple_task_id;


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


  public int getFlowchart_id() {
    return flowchart_id;
  }

  public void setFlowchart_id(int flowchart_id) {
    this.flowchart_id = flowchart_id;
  }


  public String getNode_uid() {
    return node_uid;
  }

  public void setNode_uid(String node_uid) {
    this.node_uid = node_uid;
  }


  public int getIs_public() {
    return is_public;
  }

  public void setIs_public(int is_public) {
    this.is_public = is_public;
  }


  public String getStage_type() {
    return stage_type;
  }

  public void setStage_type(String stage_type) {
    this.stage_type = stage_type;
  }


  public int getMultiple_task_id() {
    return multiple_task_id;
  }

  public void setMultiple_task_id(int multiple_task_id) {
    this.multiple_task_id = multiple_task_id;
  }

}
