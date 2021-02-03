package org.zju.vipa.aix.container.common.db.entity.aix;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

public class Source implements Serializable{
  @Id
  @GeneratedValue(generator = "JDBC")
  private Integer id;

  /**PIP,APT**/
  private String type;
  private String name;
  private String url;

  public Source() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

}
