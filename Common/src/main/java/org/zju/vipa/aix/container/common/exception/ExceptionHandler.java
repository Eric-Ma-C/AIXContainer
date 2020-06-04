package org.zju.vipa.aix.container.common.exception;

/**
 * @Date: 2020/5/23 15:48
 * @Author: EricMa
 * @Description: 异常处理
 */
public interface ExceptionHandler {

    void handle(Throwable e);

    void handle(Throwable e, String worningInfo);

}
