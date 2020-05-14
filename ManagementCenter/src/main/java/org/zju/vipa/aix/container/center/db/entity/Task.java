package org.zju.vipa.aix.container.center.db.entity;

import org.zju.vipa.aix.container.center.env.EnvError;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Date: 2020/3/19 17:07
 * @Author: EricMa
 * @Description:
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**任务id**/
    private String id;

    /**任务名**/
    private String name;

    /**创建人**/
    private String createdBy;

    /**任务类型**/
    private String type;

    /**是否为公开任务**/
    private String accessType;

    /**数据集id**/
    private String datasetId;

    /**任务的备注信息，可以用于指明任务要求**/
    private String info;

    /**更新时间**/
    private java.util.Date updatedTime;

    /**创建时间**/
    private java.util.Date createdTime;

    /**任务状态**/
    private String status;

    /**模型id**/
    private String modelId;

    /**模型提供者id**/
    private String modelProvider;
    /** 自定义启动参数 */
    private String modelArgs;

    /**设备id**/
    private String trainBy;

    /**训练细节**/
    private String trainDetail;
    /**
     * 代码路径,一般在下载model解压后路径，也可能自定义
     */
    private transient String codePath;
    /**
     * 任务环境配置过程中的报错列表
     */
    private transient ConcurrentLinkedQueue<EnvError> errorQueue;

    public Task() {
    }

    @Override
    public String toString() {
        return "Task{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", type='" + type + '\'' +
            ", accessType='" + accessType + '\'' +
            ", datasetId='" + datasetId + '\'' +
            ", info='" + info + '\'' +
            ", updatedTime=" + updatedTime +
            ", createdTime=" + createdTime +
            ", status='" + status + '\'' +
            ", modelId='" + modelId + '\'' +
            ", modelProvider='" + modelProvider + '\'' +
            ", modelArgs='" + modelArgs + '\'' +
            ", trainBy='" + trainBy + '\'' +
            ", trainDetail='" + trainDetail + '\'' +
            ", codePath='" + codePath + '\'' +
            '}';
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public ConcurrentLinkedQueue<EnvError> getErrorQueue() {
        if (errorQueue == null) {
            errorQueue = new ConcurrentLinkedQueue<>();
        }
        return errorQueue;
    }

    public void addEnvError(EnvError envError) {
        if (errorQueue == null) {
            errorQueue = new ConcurrentLinkedQueue<>();
        }
        errorQueue.offer(envError);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelProvider() {
        return modelProvider;
    }

    public void setModelProvider(String modelProvider) {
        this.modelProvider = modelProvider;
    }

    public String getModelArgs() {
        return modelArgs;
    }

    public void setModelArgs(String modelArgs) {
        this.modelArgs = modelArgs;
    }

    public String getTrainBy() {
        return trainBy;
    }

    public void setTrainBy(String trainBy) {
        this.trainBy = trainBy;
    }

    public String getTrainDetail() {
        return trainDetail;
    }

    public void setTrainDetail(String trainDetail) {
        this.trainDetail = trainDetail;
    }
}
