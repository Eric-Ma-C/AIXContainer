package org.zju.vipa.aix.container.center.db.entity;
import java.io.Serializable;

public class Device implements Serializable{


  /**设备id**/
  private String id;

  /**创建人**/
  private String createdBy;

  /**自定义设备名**/
  private String name;

  /**token**/
  private String token;

  /**创建时间**/
  private java.util.Date createdTime;

  /**更新时间**/
  private java.util.Date updatedTime;

  /**设备信息**/
  private String info;
  private String detail;


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


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
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


  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

}
