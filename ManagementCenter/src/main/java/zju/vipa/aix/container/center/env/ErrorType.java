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
    CUDA_OUT_OF_MEMORY("RuntimeError: CUDA out of memory"),
    /**
     * conda找不到相关安装包,一般需要用pip安装
     */
    RESOLVE_PACKAGE_NOT_FOUND("ResolvePackageNotFound"),
    /**
     *  编码问题
     *  "PYTHONIOENCODING=utf-8 python -u -m  recognition.main -name prov_test"
     */
    UNICODE_ENCODE_ERROR("UnicodeEncodeError"),
    /**
     * numpy版本太高，没有bit_generator
     * 降级安装
     * "pip uninstall numpy -y",
     *         "pip install -U numpy==1.17.0",
     */
    NUMPY_RANDOM_HAS_NO_ATTRIBUTE_BIT_GENERATOR("AttributeError: module 'numpy.random' has no attribute 'bit_generator'");





    private String keyWords;

    ErrorType(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getKeyWords() {
        return keyWords;
    }


//
//    IpoM8-nAs: ERROR: Could not install packages due to an EnvironmentError: HTTPSConnectionPool(host='files.pythonhosted.org', port=443): Max retries exceeded with url: /packages/11/df/5a3bba95b4600d5ca7aff072082ef0d9837056dd28cc4e738e7ce88dd8f8/imgaug-0.3.0-py2.py3-none-any.whl
//    (Caused by ConnectTimeoutError(<pip._vendor.urllib3.connection.VerifiedHTTPSConnection object at 0x7fda1f71b630>,
//    'Connection to files.pythonhosted.org timed out. (connect timeout=15)'))
//        22:19:36,435 [INFO] (aix-t1):
//    IpoM8-nAs: resultCode=1

}
