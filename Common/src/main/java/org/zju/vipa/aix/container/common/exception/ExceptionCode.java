package org.zju.vipa.aix.container.common.exception;

/**
 * @Date: 2020/5/27 9:19
 * @Author: EricMa
 * @Description: 错误码接口
 */
public interface ExceptionCode {
    /**
     * 获取错误码
     * @return 错误码
     */
    String getCode();

    /**
     * 获取错误信息
     * @return 错误信息
     */
    String getDescription();
}
