package org.zju.vipa.aix.container.common.db.entity.aix;
import java.io.Serializable;

public class User implements Serializable{


  /**ID**/
  private String id;

  /**邮箱**/
  private String email;

  /**用户名**/
  private String username;

  /**角色**/
  private String type;

  /**密码**/
  private String password;

  /**创建时间 用户创建时间**/
  private java.util.Date createdTime;

  /**更新时间 用户信息更新时间**/
  private java.util.Date updatedTime;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

}
