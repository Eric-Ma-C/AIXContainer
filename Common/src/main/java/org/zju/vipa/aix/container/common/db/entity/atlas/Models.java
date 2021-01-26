package org.zju.vipa.aix.container.common.db.entity.atlas;
import java.io.Serializable;

public class Models implements Serializable{

  private int id;
  private String modelName;
  private String datasetName;
  private String md5;
  private String task;
  private String file;
  private int isPublic;
  private java.util.Date createdTime;
  private int userId;
  private String info;
  private String args;
  private String metrics;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }


  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }


  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }


  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }


  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }


  public int getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(int isPublic) {
    this.isPublic = isPublic;
  }


  public java.util.Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(java.util.Date createdTime) {
    this.createdTime = createdTime;
  }


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }


  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }


  public String getArgs() {
    return args;
  }

  public void setArgs(String args) {
    this.args = args;
  }


  public String getMetrics() {
    return metrics;
  }

  public void setMetrics(String metrics) {
    this.metrics = metrics;
  }

}
