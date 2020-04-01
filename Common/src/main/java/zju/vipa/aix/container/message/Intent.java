package zju.vipa.aix.container.message;


/**
 * @Date: 2020/1/9 22:02
 * @Author: EricMa
 * @Description: 消息意图, 主题
 */
public enum Intent {

    /**
     * 空消息
     * 若容器收到此消息，则开始心跳线程
     */
    NULL,
    /**
     * 注册
     */
    REGISTER,
    /**
     * gpu信息
     */
    GPU_INFO,
    /**
     * 容器心跳轮询
     */
    HEARTBEAT,
    /**
     * 执行特定任务
     */
    TASK,
    /**
     * 执行特定的shell指令
     */
    SHELL_TASK,
    /**
     * 请求conda国内源
     */
    CONDA_SOURCE,


    /**
     * 请求conda环境文件
     */
    GET_CONDA_ENV_FILE_BY_TASKID,
    /**
     * 请求pip环境文件
     */
    GET_PIP_ENV_FILE_BY_TASKID,
    /**
     * 返回conda环境文件url
     */
    CONDA_ENV_FILE_URL,
    /**
     * 返回pip环境文件url
     */
    PIP_ENV_FILE_URL,


    /**
     * 请求新任务，平台返回待发送消息
     * 若平台没有待发送消息，返回HEARTBEAT，容器开始心跳汇报
     */
    ASK_FOR_WORK,



    /**
     * 请求任务代码
     */
    GET_TASK_CODE,
    /**
     * 返回任务代码文件url
     */
    TASK_CODE_URL,
    /**
     * 请求任务数据
     */
    GET_TASK_DATA,
    /**
     * 返回任务数据文件url
     */
    TASK_DATA_URL,


    /**
     * shell指令开始执行
     */
    SHELL_BEGIN,
    /**
     * shell指令执行信息
     */
    SHELL_INFO,
    /**
     * shell指令执行错误信息
     */
    SHELL_ERROR,
    /**
     * shell指令执行结果
     */
    SHELL_RESULT,
//        /** conda环境配置错误信息 */
//        condaError,
//        /** conda环境配置错误信息 */
//        condaError,
//        /** conda环境配置错误信息 */
//        condaError,
    /**
     * 容器java异常上传
     */
    EXCEPTION,
    /**
     * 容器实时日志上传
     */
    REAL_TIME_LOG;

    @Override
    public String toString() {
        return this.name();
    }
}
