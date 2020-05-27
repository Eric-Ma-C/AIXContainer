package org.zju.vipa.aix.container.exception;

/**
 * @Date: 2020/5/27 9:20
 * @Author: EricMa
 * @Description: 业务错误码
 */
public enum  ErrorCodeEnum implements ErrorCode{

    /** 未指明的异常 */
    UNSPECIFIED("-1", "未知异常，请稍后再试"),

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
    private ErrorCodeEnum(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码查询枚举。
     *
     * @param code 编码。
     * @return 枚举。
     */
    public static ErrorCodeEnum getByCode(String code) {
        for (ErrorCodeEnum value : ErrorCodeEnum.values()) {
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
        for (ErrorCodeEnum value : ErrorCodeEnum.values()) {
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
