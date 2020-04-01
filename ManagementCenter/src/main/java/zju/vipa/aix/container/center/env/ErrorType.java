package zju.vipa.aix.container.center.env;

/**
 * @Date: 2020/3/31 18:20
 * @Author: EricMa
 * @Description: 运行过程中的错误类型
 */
public enum ErrorType {
    /**
     * 未知错误
     */
    UNKNOWN("Unknown error,or not important info"),
    /**
     * 任务运行报错，一般可以通过pip安装对应的库
     */
    MODULE_NOT_FOUND("ModuleNotFoundError: No module named"),
    /**
     * 已有同名conda环境
     */
    CONDA_PREFIX_ALREADY_EXISTS("CondaValueError: prefix already exists"),
    /**
     * conda环境未找到
     */
    CONDA_PREFIX_NOT_FOUND("Could not find conda environment"),
    /**
     * cuda内存耗尽
     */
    CUDA_OUT_OF_MEMORY("RuntimeError: CUDA out of memory");

    private String keyWords;

    ErrorType(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getKeyWords() {
        return keyWords;
    }
}
