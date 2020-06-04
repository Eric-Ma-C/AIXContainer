package org.zju.vipa.aix.container.common.exception;

/**
 * @Date: 2020/5/27 9:20
 * @Author: EricMa
 * @Description: 业务错误码
 */
public enum ExceptionCodeEnum implements ExceptionCode {

    /** 未指明的异常 */
    UNSPECIFIED("-1", "未定义异常"),


    /** 单例异常 */
    SINGLETON_MULTI_INSTANCE("10001", "单例模式不可以创建多个对象"),
    FILE_UPLOAD_FAILED("10002", "文件上传失败"),
    UPLOAD_NOT_PERMITTED("10003", "服务器忙，文件上传被拒绝"),
    SERVER_NOT_RESPONSE("10004", "服务器没有响应，文件上传失败"),



    SERVER_ERROR("500", "网络异常，请稍后再试"),
    NO_SERVICE("404", "网络异常, 服务器熔断"),

    // 通用异常
    REQUEST_ERROR("400", "入参异常,请检查入参后再次调用"),
    PAGE_NUM_IS_NULL("4001","页码不能为空"),
    PAGE_SIZE_IS_NULL("4002","页数不能为空"),
    ID_IS_NULL("4003","ID不能为空"),
    SEARCH_IS_NULL("4004","搜索条件不能为空");

    /** 错误码 */
    private final String code;

    /** 描述 */
    private final String description;

    /**
     * @param code 错误码
     * @param description 描述
     */
    private ExceptionCodeEnum(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码查询枚举。
     *
     * @param code 编码。
     * @return 枚举。
     */
    public static ExceptionCodeEnum getByCode(String code) {
        for (ExceptionCodeEnum value : ExceptionCodeEnum.values()) {
            if (value.getCode().equals(code )) {
                return value;
            }
        }
        return UNSPECIFIED;
    }

    /**
     * 枚举是否包含此code
     * 注意在 cluster 环境下，每个虚拟机都会构造出一个同义的枚举对象。
     * 因而在做比较操作时候就需要注意，如果直接通过使用等号
     * ( ‘ == ’ ) 操作符，这些看似一样的枚举值一定不相等，
     * 因为这不是同一个对象实例。
     * @param code 枚举code
     * @return 是否包含
     */
    public static Boolean contains(String code){
        for (ExceptionCodeEnum value : ExceptionCodeEnum.values()) {
            if (value.getCode().equals(code)) {
                return true;
            }
        }
        return  false;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }


}
