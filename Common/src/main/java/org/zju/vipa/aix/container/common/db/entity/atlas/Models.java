package org.zju.vipa.aix.container.common.db.entity.atlas;
import java.io.Serializable;

public class Models implements Serializable{

  private int id;
  private String model_name;
  private String dataset_name;
  private String md5;
  private String task;
  private String file;
  private int is_public;
  private java.util.Date created_time;
  private int user_id;
  private String info;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getModel_name() {
    return model_name;
  }

  public void setModel_name(String model_name) {
    this.model_name = model_name;
  }


  public String getDataset_name() {
    return dataset_name;
  }

  public void setDataset_name(String dataset_name) {
    this.dataset_name = dataset_name;
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


  public int getIs_public() {
    return is_public;
  }

  public void setIs_public(int is_public) {
    this.is_public = is_public;
  }


  public java.util.Date getCreated_time() {
    return created_time;
  }

  public void setCreated_time(java.util.Date created_time) {
    this.created_time = created_time;
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

}
