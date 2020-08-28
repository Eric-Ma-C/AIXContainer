package org.zju.vipa.aix.container.common.db.entity.aix;
import java.io.Serializable;

public class Model implements Serializable{


  /**模型id**/
  private String id;

  /**拥有者id**/
  private String createdBy;

  /**模型名**/
  private String name;

  /**数据集类型**/
  private String taskType;
  private String status;

  /**模型路径**/
  private String codePath;

  /**配置**/
  private String config;

  /**公开**/
  private String accessType;

  /**创建时间**/
  private java.util.Date createdTime;

  /**更新时间**/
  private java.util.Date updatedTime;

  /**模型信息备注**/
  private String info;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getCodePath() {
    return codePath;
  }

  public void setCodePath(String codePath) {
    this.codePath = codePath;
  }


  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }


  public String getAccessType() {
    return accessType;
  }

  public void setAccessType(String accessType) {
    this.accessType = accessType;
  }


  public java.util.Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(java.util.Date createdTime) {
    this.createdTime = createdTime;
  }


  public java.util.Date getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(java.util.Date updatedTime) {
    this.updatedTime = updatedTime;
  }


  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

}
