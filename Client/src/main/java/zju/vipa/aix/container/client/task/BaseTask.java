package zju.vipa.aix.container.client.task;


/**
 * @Date: 2020/3/10 9:23
 * @Author: EricMa
 * @Description: 封装容器基本任务，所有任务的基类
 */
public abstract class BaseTask {
    private TaskStateListener listener;
    /**
     * 状态
     */
    private volatile TaskState state;
    /**
     * shell指令序列
     */
    private String[] commands;
    /**
     * 指令输出
     */
    private String output;
    /**
     * 已运行时长
     */
    private long execTime = 0;

    public TaskState getState() {
        return state;
    }

//    public void setState(TaskState state) {
//        this.state = state;
//    }

    public String[] getCommands() {
        if (commands == null) {
            setCommands(initTaskCmds());
        }
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

//    public String getOutput() {
//        return output;
//    }
//
//    public void setOutput(String output) {
//        this.output = output;
//    }
//
//    public long getExecTime() {
//        return execTime;
//    }
//
//    public void setExecTime(long execTime) {
//        this.execTime = execTime;
//    }

    protected BaseTask() {
        state = TaskState.WAITING;
    }

    /**
     * 执行任务
     *
     * @param taskStateListener 任务执行状态回调
     * @return: void
     */
    public void run(TaskStateListener taskStateListener) {
        this.listener = taskStateListener;

        listener.onBegin();
        state = TaskState.RUNNING;
        procedure();
        state = TaskState.FINISHED;
        listener.onFinished();
    }

    /**
     * 定义Task的shell命令
     *
     * @param:
     * @return:
     */
    abstract protected String[] initTaskCmds();

    /**
     * 定义执行流程
     *
     * @param:
     * @return:
     */
    abstract protected void procedure();

    /**
     * 任务执行完毕回调
     */
    public interface TaskStateListener {
        void onBegin();

        void onFinished();
    }

    ;
}
