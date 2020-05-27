package org.zju.vipa.aix.container.exception;

/**
 * @Date: 2020/5/27 9:15
 * @Author: EricMa
 * @Description: AIX自定义异常基类
 */
public class BaseException extends RuntimeException {
    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
