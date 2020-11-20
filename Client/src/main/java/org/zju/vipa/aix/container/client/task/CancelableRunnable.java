package org.zju.vipa.aix.container.client.task;

import java.util.concurrent.RunnableFuture;

/**
 * @Date: 2020/11/20 14:52
 * @Author: EricMa
 * @Description: 可取消的Runnable
 */
public interface CancelableRunnable<V> extends Runnable{
     void cancel();
     RunnableFuture<V> newTask();
}
