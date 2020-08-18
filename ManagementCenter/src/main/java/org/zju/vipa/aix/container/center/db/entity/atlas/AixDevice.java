package org.zju.vipa.aix.container.center.db.entity.atlas;
import java.io.Serializable;

public class AixDevice implements Serializable{

  private int id;
  private String device_name;
  private java.util.Date created_time;
  private java.util.Date last_login;
  private String detail;
  private String info;
  private String token;
  private int user_id;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getDevice_name() {
    return device_name;
  }

  public void setDevice_name(String device_name) {
    this.device_name = device_name;
  }


  public java.util.Date getCreated_time() {
    return created_time;
  }

  public void setCreated_time(java.util.Date created_time) {
    this.created_time = created_time;
  }


  public java.util.Date getLast_login() {
    return last_login;
  }

  public void setLast_login(java.util.Date last_login) {
    this.last_login = last_login;
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


  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

}
