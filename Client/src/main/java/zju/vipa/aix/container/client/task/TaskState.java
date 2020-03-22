package zju.vipa.aix.container.client.task;

/**
 * @Date: 2020/3/10 9:28
 * @Author: EricMa
 * @Description: 容器任务状态
 */
public enum TaskState {
    /** 等待执行 */
    WAITING,
    /** 执行 */
    RUNNING,
    /** 执行完毕 */
    FINISHED;

    @Override
    public String toString() {
        return this.toString();
    }
}
