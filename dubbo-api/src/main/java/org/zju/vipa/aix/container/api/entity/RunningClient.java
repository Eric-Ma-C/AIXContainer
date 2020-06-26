package org.zju.vipa.aix.container.api.entity;

import org.zju.vipa.aix.container.common.message.GpuInfo;

import java.io.Serializable;

/**
 * @Date: 2020/6/4 16:41
 * @Author: EricMa
 * @Description: 正在运行的容器信息
 */
public class RunningClient implements Serializable {

    String id;
    String token;
    /**
     * 连入平台的时间
     */
    String since;
    GpuInfo gpuInfo;
    TaskBriefInfo taskBriefInfo;
    /**
     * 正在运行的指令
     */
    String runningCmds;

    public RunningClient() {
    }

    public RunningClient(String id,String token, String since) {
        this.id = id;
        this.token=token;
        this.since = since;
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

    public TaskBriefInfo getTaskBriefInfo() {
        return taskBriefInfo;
    }

    public void setTaskBriefInfo(TaskBriefInfo taskBriefInfo) {
        this.taskBriefInfo = taskBriefInfo;
    }

    public String getRunningCmds() {
        return runningCmds;
    }

    public void setRunningCmds(String runningCmds) {
        this.runningCmds = runningCmds;
    }
}
