package org.zju.vipa.aix.container.common.db.entity.aix;

import org.zju.vipa.aix.container.common.utils.TimeUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FinishedTask implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private int id;

    /**
     * 执行该任务的设备id
     **/
    private int deviceId;

    /**
     * 执行任务的id,一个任务可能被多次执行
     **/
    private int taskId;

    /**
     * 任务状态(SUCCESS,FAILED,CANCEL)
     **/
    private String status;

    /**
     * 开始时间
     **/
    private java.util.Date begin;

    /**
     * 结束时间
     **/
    private java.util.Date end;

    /**
     * 持续时长,单位分钟
     **/
    private int durationMin;

    /**
     * 任务日志,json数组
     **/
    private String logs;

    public FinishedTask() {
    }

    public FinishedTask(String deviceId, String taskId, String status, Date begin, Date end, List<String> logs) {
        this.deviceId = Integer.parseInt(deviceId);
        this.taskId = Integer.parseInt(taskId);
        this.status = status;
        this.begin = begin;
        this.end = end;
        this.durationMin = TimeUtils.dateMinusMin(end, begin);
        StringBuilder sb = new StringBuilder();
        for (String log : logs) {
            sb.append(log);
        }
        this.logs = sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public java.util.Date getBegin() {
        return begin;
    }

    public void setBegin(java.util.Date begin) {
        this.begin = begin;
    }


    public java.util.Date getEnd() {
        return end;
    }

    public void setEnd(java.util.Date end) {
        this.end = end;
    }


    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }


    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "FinishedTask{" +
            "id=" + id +
            ", deviceId=" + deviceId +
            ", taskId=" + taskId +
            ", status='" + status + '\'' +
            ", begin=" + begin +
            ", end=" + end +
            ", durationMin=" + durationMin +
            ", logs='" + logs + '\'' +
            '}';
    }
}
