package org.zju.vipa.aix.container.api.entity;

import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;
import org.zju.vipa.aix.container.common.message.GpuInfo;
import org.zju.vipa.aix.container.common.utils.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/6/4 16:41
 * @Author: EricMa
 * @Description: 正在运行的容器信息, 用于前端展示
 */
public class RunningClient implements Serializable {

    String id;
    String name;
    String token;
    /**
     * 连入平台的时间
     */
    String since;
    String userId;
    String info;
    /**
     * 最后一次向平台显示心跳的时间
     */
    transient long lastHeartbeat;

    GpuInfo gpuInfo;

    String firstTaskName;
    List<TaskBriefInfo> taskBriefInfoList;

    /**
     * 正在运行的指令
     */
    String runningCmds;
    /**
     * 最近一次报错信息，一般有多行
     */
    String latestErrors;
    /**
     * 最后一次报错时间，用于区分Error批次
     */
    transient long lastErrorTime;

    public RunningClient() {
    }

    @Deprecated
    public RunningClient(String id, String token, String since) {
        this.id = id;
        this.token = token;
        this.since = since;
        this.taskBriefInfoList = new ArrayList<>();
        this.firstTaskName = "No Task";
        this.runningCmds="";
        this.latestErrors="";

    }

    public RunningClient(AixDevice device) {

        this.name = device.getDevice_name();
        this.info = device.getInfo();
        this.userId = String.valueOf(device.getUser_id());
        this.id = String.valueOf(device.getId());
        this.token = device.getToken();
        this.since = TimeUtils.getCurrentTimeStr();
        this.taskBriefInfoList = new ArrayList<>();
        this.firstTaskName = "No Task";
        this.runningCmds="";
        this.latestErrors="";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public GpuInfo getGpuInfo() {
        return gpuInfo;
    }

    public void setGpuInfo(GpuInfo gpuInfo) {
        this.gpuInfo = gpuInfo;
    }

    public List<TaskBriefInfo> getTaskBriefInfoList() {
        return taskBriefInfoList;
    }

    public void setTaskBriefInfoList(List<TaskBriefInfo> taskBriefInfoList) {
        this.taskBriefInfoList = taskBriefInfoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRunningCmds() {
        return runningCmds;
    }

    public void setRunningCmds(String runningCmds) {
        this.runningCmds = runningCmds;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public String getFirstTaskName() {
        return firstTaskName;
    }

    public void setFirstTaskName(String firstTaskName) {
        this.firstTaskName = firstTaskName;
    }

    public String getLatestErrors() {
        return latestErrors;
    }

    public void setLatestErrors(String latestErrors) {
        this.latestErrors = latestErrors;
    }

    /** 新增一条错误信息，按时间自动切分
     * @param error
     * @return: void
     */
    public void addLatestErrors(String error) {
        long millis = System.currentTimeMillis();
        if (millis - lastErrorTime > 2000) {
            clearLatestErrors();
        }
        error = error + "\n";
        latestErrors = latestErrors + error;

        lastErrorTime = millis;
    }

    private void clearLatestErrors() {
        this.latestErrors = TimeUtils.getCurrentTimeStr()+"\n";
    }

    public void setTaskBriefInfo(TaskBriefInfo briefInfo) {
        this.taskBriefInfoList.clear();
        if (briefInfo != null) {
            this.taskBriefInfoList.add(briefInfo);
            firstTaskName = briefInfo.getName();
        } else {
            firstTaskName = "No Task";
        }
    }


}
