package org.zju.vipa.aix.container.common.db.entity.aix;


import org.zju.vipa.aix.container.common.config.Config;
import org.zju.vipa.aix.container.common.db.entity.atlas.TaskTask;
import org.zju.vipa.aix.container.common.env.EnvError;
import org.zju.vipa.aix.container.common.json.Fields;
import org.zju.vipa.aix.container.common.json.TaskInfo;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Date: 2020/3/19 17:07
 * @Author: EricMa
 * @Description:
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 任务id
     **/
    private String id;

    /**
     * 任务名
     **/
    private String name;

    /**
     * 创建人
     **/
    private String createdBy;

    /**
     * 任务类型
     **/
    private String type;

    /**
     * 是否为公开任务
     **/
    private String accessType;

    /**
     * 数据集id
     **/
    private String datasetId;

    /**
     * 任务的备注信息，可以用于指明任务要求
     **/
    private String info;

    /**
     * 更新时间
     **/
    private java.util.Date updatedTime;

    /**
     * 创建时间
     **/
    private java.util.Date createdTime;

    /**
     * 任务状态
     **/
    private String status;

    /**
     * 模型id
     **/
    private String modelId;

    /**
     * 模型提供者id
     **/
    private String modelProvider;
    /**
     * 自定义启动参数
     */
    private String modelArgs;

    /**
     * 设备id
     **/
    private String trainBy;

    /**
     * 训练细节
     **/
    private String trainDetail;
    /**
     * 代码路径,一般在下载model解压后路径，也可能自定义
     */
    private transient String codePath;
    /**
     * 任务环境配置过程中的报错列表
     */
    private transient ConcurrentLinkedQueue<EnvError> errorQueue;
//    /**
//     * 本任务最后一条执行报错信息
//     */
//    private transient String lastErrorInfo;



    /**
     * 该任务执行过程中连续出现未解决错误的次数，超过一定阈值判断为环境配置失败
     */
    private transient int unknownErrorTime;

    /**
     * 任务环境配置失败的标志（数据库置为FAILED），askForCmds会检查该字段，失败则告诉客户端放弃此任务，重新抢任务
     */
    private transient boolean isFailed=false;

    public Task(TaskTask tt) {

        TaskInfo serializedInfo= JsonUtils.parseObject(tt.getTask(),TaskInfo.class);


        id = tt.getId() + "";
        name = tt.getName();
        type = serializedInfo.getTask_args().getTasks().get(0);
        accessType = tt.getAccess_type();
        datasetId = String.valueOf(serializedInfo.getTask_args().getDatasets().get(0).getId());// null   ;
        info = tt.getInfo();
        updatedTime = tt.getStarted_time();
        createdTime = tt.getCreated_time();
        status = tt.getStatus();
        modelId = serializedInfo.getTask_args().getTeacher_models().get(0).getId() + "";
        modelProvider = tt.getUser_id() + "";
        modelArgs = "";
        List<Fields> fieldsList = serializedInfo.getTask_args().getAlgorithms().getFields();
        for (Fields field : fieldsList) {
           if ("model_args".equals(field.getField_name())) {
               modelArgs=field.getField_value();
               break;
           }
       }

        trainBy = tt.getTrain_by_id() + "";
        trainDetail = tt.getNote();
        codePath = Config.MODEL_UNZIP_PATH;
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

    public void addUnknownErrorTime() {
        /** 连续出现500次未知错误则放弃任务 */
        if (this.unknownErrorTime ++>500) {
            isFailed=true;
        }
    }
    public void clearUnknownErrorTime() {
            unknownErrorTime=0;
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

    public boolean isFailed() {
        return isFailed;
    }
}
