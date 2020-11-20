package org.zju.vipa.aix.container.common.db.entity.atlas;
import java.io.Serializable;

public class Codes implements Serializable{

  private int id;
  private String name;
  private String task;
  private String file;
  private String pre_cmd;
  private String args;
  private String metrics;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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


  public String getPre_cmd() {
    return pre_cmd;
  }

  public void setPre_cmd(String pre_cmd) {
    this.pre_cmd = pre_cmd;
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
