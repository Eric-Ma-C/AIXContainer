package org.zju.vipa.aix.container.center.db.entity.atlas;
import java.io.Serializable;

public class AixDevice implements Serializable{

  private int id;
  private String deviceName;
  private java.util.Date createdTime;
  private java.util.Date lastLogin;
  private String detail;
  private String info;
  private String token;
  private int userId;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }


  public java.util.Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(java.util.Date createdTime) {
    this.createdTime = createdTime;
  }


  public java.util.Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(java.util.Date lastLogin) {
    this.lastLogin = lastLogin;
  }


  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }


  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

}
