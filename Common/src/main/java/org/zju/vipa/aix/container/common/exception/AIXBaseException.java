package org.zju.vipa.aix.container.common.exception;

/**
 * @Date: 2020/5/27 9:15
 * @Author: EricMa
 * @Description: AIX自定义异常基类
 */
public class AIXBaseException extends RuntimeException {
    /** 错误码 */
    protected final ExceptionCode exceptionCode;

    /**
     * 无参默认构造UNSPECIFIED
     */
    public AIXBaseException() {
        super(ExceptionCodeEnum.UNSPECIFIED.getDescription());
        this.exceptionCode = ExceptionCodeEnum.UNSPECIFIED;
    }

    /**
     * 指定错误码构造通用异常
     * @param exceptionCode 错误码
     */
    public AIXBaseException(final ExceptionCode exceptionCode) {
        super(exceptionCode.getDescription());
        this.exceptionCode = exceptionCode;
    }

    /**
     * 指定详细描述构造通用异常
     * @param detailedMessage 详细描述
     */
    public AIXBaseException(final String detailedMessage) {
        super(detailedMessage);
        this.exceptionCode = ExceptionCodeEnum.UNSPECIFIED;
    }

    /**
     * 指定导火索构造通用异常
     * @param t 异常原因
     */
    public AIXBaseException(final Throwable t) {
        super(t);
        this.exceptionCode = ExceptionCodeEnum.UNSPECIFIED;
    }

    /**
     * 构造通用异常
     * @param exceptionCode 错误码
     * @param detailedMessage 详细描述
     */
    public AIXBaseException(final ExceptionCode exceptionCode, final String detailedMessage) {
        super(detailedMessage);
        this.exceptionCode = exceptionCode;
    }

    /**
     * 构造通用异常
     * @param exceptionCode 错误码
     * @param t 异常原因
     */
    public AIXBaseException(final ExceptionCode exceptionCode, final Throwable t) {
        super(exceptionCode.getDescription(), t);
        this.exceptionCode = exceptionCode;
    }

    /**
     * 构造通用异常
     * @param detailedMessage 详细描述
     * @param t 异常原因
     */
    public AIXBaseException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.exceptionCode = ExceptionCodeEnum.UNSPECIFIED;
    }

    /**
     * 构造通用异常
     * @param exceptionCode 错误码
     * @param detailedMessage 详细描述
     * @param t 异常原因
     */
    public AIXBaseException(final ExceptionCode exceptionCode, final String detailedMessage,
                            final Throwable t) {
        super(detailedMessage, t);
        this.exceptionCode = exceptionCode;
    }

    /**
     * Getter method for property <tt>errorCode</tt>.
     * @return property value of errorCode
     */
    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

}
