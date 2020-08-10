package org.zju.vipa.aix.container.center.db.entity.atlas;
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
  private String code_path;
  private String info;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getModelName() {
    return model_name;
  }

  public void setModelName(String model_name) {
    this.model_name = model_name;
  }


  public String getDatasetName() {
    return dataset_name;
  }

  public void setDatasetName(String dataset_name) {
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


  public int getIsPublic() {
    return is_public;
  }

  public void setIsPublic(int is_public) {
    this.is_public = is_public;
  }


  public java.util.Date getCreatedTime() {
    return created_time;
  }

  public void setCreatedTime(java.util.Date created_time) {
    this.created_time = created_time;
  }


  public int getUserId() {
    return user_id;
  }

  public void setUserId(int user_id) {
    this.user_id = user_id;
  }

  public String getCodePath() {
    return code_path;
  }

  public void setCodePath(String code_path) {
    this.code_path = code_path;
  }


  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

}
