package org.zju.vipa.aix.container.client.task;

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
    /** 被中断状态，正在停止 */
    STOPPING,
    /** 执行完毕,停止状态*/
    STOPPED;

    @Override
    public String toString() {
        return this.toString();
    }

    /**
     * 比较是否相同
     * 由于 enum 类型的值实际上是通过运行期构造出对象来表示的，
     * 所以在 cluster 环境下，每个虚拟机都会构造出一个同义的枚举对象。
     * 因而在做比较操作时候就需要注意，如果直接通过使用等号 ( ‘ == ’ ) 操作符，
     * 这些看似一样的枚举值一定不相等，因为这不是同一个对象实例。
     *
     * @param state
     * @return: boolean
     */
    public boolean match(TaskState state) {
        if (state == null) {
            return false;
        }
        return this.name().equals(state.name());
    }
}
