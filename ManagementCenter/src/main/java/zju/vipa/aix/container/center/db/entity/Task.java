package zju.vipa.aix.container.center.db.entity;

import zju.vipa.aix.container.config.AIXEnvConfig;
import zju.vipa.aix.container.center.env.EnvError;

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

    /**
     * 任务id     primary key
     */
    private String id;

    /**
     * 任务名
     */
    private String name;

    /**
     * 创建人
     */
    private String createdby;

    /**
     * 任务类型
     */
    private String type;

    /**
     * 是否为公开任务
     */
    private String accesstype;

    /**
     * 数据集id
     */
    private String datasetid;

    /**
     * 任务的备注信息，可以用于指明任务要求
     */
    private String info;

    /**
     * 更新时间
     */
    private Date updatedtime;

    /**
     * 创建时间
     */
    private Date createdtime;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 模型id
     */
    private String modelId;

    /**
     * 模型提供者id
     */
    private String modelprovider;

    /**
     * 设备id
     */
    private String trainby;

    /**
     * 训练细节
     */
    private String traindetail;
    /**
     * 代码路径
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
            ", createdby='" + createdby + '\'' +
            ", type='" + type + '\'' +
            ", accesstype='" + accesstype + '\'' +
            ", datasetid='" + datasetid + '\'' +
            ", info='" + info + '\'' +
            ", updatedtime=" + updatedtime +
            ", createdtime=" + createdtime +
            ", status='" + status + '\'' +
            ", modelId='" + modelId + '\'' +
            ", modelprovider='" + modelprovider + '\'' +
            ", trainby='" + trainby + '\'' +
            ", traindetail='" + traindetail + '\'' +
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

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccesstype() {
        return accesstype;
    }

    public void setAccesstype(String accesstype) {
        this.accesstype = accesstype;
    }

    public String getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(String datasetid) {
        this.datasetid = datasetid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(Date updatedtime) {
        this.updatedtime = updatedtime;
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
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

    public String getModelprovider() {
        return modelprovider;
    }

    public void setModelprovider(String modelprovider) {
        this.modelprovider = modelprovider;
    }

    public String getTrainby() {
        return trainby;
    }

    public void setTrainby(String trainby) {
        this.trainby = trainby;
    }

    public String getTraindetail() {
        return traindetail;
    }

    public void setTraindetail(String traindetail) {
        this.traindetail = traindetail;
    }
}
