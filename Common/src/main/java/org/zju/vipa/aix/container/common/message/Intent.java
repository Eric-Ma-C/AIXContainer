package org.zju.vipa.aix.container.common.message;


/**
 * @Date: 2020/1/9 22:02
 * @Author: EricMa
 * @Description: 消息意图, 主题
 */
public enum Intent {

    /**
     * 空消息
     * 若容器收到此消息，则开始抢任务线程
     */
    NULL,
    /**
     * 心跳ping,附带gpu信息
     * center可能回应pong,也可能回应一些控制指令
     */
    PING,
    /**
     * 心跳普通回应pong
     */
    PONG,
    /**
     * 注册
     */
    REGISTER,

    /**
     * 容器抢任务轮询
     */
    GRAB_TASK,
    /**
     * 容器抢任务失败
     */
    GRAB_TASK_FAILED,
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
     * 若平台没有待发送消息，返回GRAB_TAST，容器开始抢任务
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
     * shell指令执行错误信息,不需要平台解析，只记录log
     */
    SHELL_ERROR,
    /**
     * shell指令执行错误信息,寻求平台解析
     */
    SHELL_ERROR_HELP,
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
     * 容器实时日志上传,默认简要模式
     */
    REAL_TIME_LOG,
    /**
     * 进入更多容器实时日志上传状态,需要指定clientId
     */
    REAL_TIME_LOG_SHOW_DETAIL,
    /**
     * 进入更少容器实时日志上传状态(默认的状态)
     */
    REAL_TIME_LOG_SHOW_BRIEF,


    /**
     * 容器请求上传
     */
    REQUEST_UPLOAD,
    /**
     * 允许容器上传
     */
    UPLOAD_PERMITTED,
    /**
     * 容器上传数据的连接
     */
    UPLOAD_DATA,
    /**
     * 容器（日志）文件上传成功
     */
    UPLOAD_SUCCESS,


    /**
     * 容器下载model
     */
    DOWNLOAD_MODEL,
    /**
     * 容器下载dataset
     */
    DOWNLOAD_DATASET;

    @Override
    public String toString() {
        return this.name();
    }

    /**
     * 比较是否相同
     * 由于 enum 类型的值实际上是通过运行期构造出对象来表示的，
     * 所以在 cluster 环境下，每个虚拟机都会构造出一个同义的枚举对象。
     * 因而在做比较操作时候就需要注意，如果直接通过使用等号 ( ‘ == ’ ) 操作符，
     * 这些看似一样的枚举值一定不相等，因为这不是同一个对象实例。
     *
     * @param intent
     * @return: boolean
     */
    public boolean match(Intent intent) {
        if (intent == null) {
            return false;
        }
        return this.name().equals(intent.name());
    }

    /**
     * 比较是否相同
     * @param msg
     * @return: boolean
     */
    public boolean match(Message msg) {
        if (msg==null) {
            return false;
        }
        return this.name().equals(msg.getIntent().name());
    }
}
