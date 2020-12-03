package org.zju.vipa.aix.container.common.env;

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
     * preCmds错误
     */
    PRE_CMDS_ERROR("PRE_CMDS_ERROR"),
    /**
     * 网络连接不稳定
     */
    HTTP_RETRY("HTTP errors are often intermittent, and a simple retry will get you on your way."),

    /**
     * 网络连接不稳定导致apt-get update失败
     */
    APT_RETRY("Some index files failed to download."),
    /**
     * 网络连接不稳定导致conda-env create失败
     */
    CONDA_CREATE_RETRY("An unexpected error has occurred. Conda has prepared the above report."),
    /**
     * apt-get GPG error 重新
     */
    APT_GPG_ERRER("Release: The following signatures were invalid:"),

    /**
     * 没有gcc
     */
    GCC_NOT_FOUND("unable to execute 'gcc': No such file or directory"),
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
    NUMPY_LEVEL_TOO_HIGH("AttributeError: module 'numpy.random' has no attribute 'bit_generator'"),
    /**
     * tensorflow版本太高，2.0以上没有contrib
     * 降级安装
     * "pip uninstall tensorflow -y",
     *         "pip install -U tensorflow==1.13.1",
     */
    TENSORFLOW_LEVEL_TOO_HIGH("AttributeError: module 'tensorflow' has no attribute 'contrib'"),
    /**
     * pip源失效
     * 换源
     */
    PIP_SOURCE_NO_MATCHING_DISTRIBUTION("No matching distribution found for"),
    /**
     * 网络超时
     * 可能是pip源失效,换源
     */
    PIP_SOURCE_READ_TIMED_OUT("ReadTimeoutError: HTTPSConnectionPool"),

    /**
     * cv2缺少c库
     *
     * apt update && apt install -y libsm6 libxext6
     */
    IMPORTERROR_LIBSM_SO_6("ImportError: libSM.so.6: cannot open shared object file: No such file or directory"),

    /**
     *   conda index cache被污染，使用conda clean -i 清理
     *   https://www.jianshu.com/p/2bca744fcd82
     *
     */
    CONDA_SOURCE_304_ERROR("raise Response304ContentUnchanged()");







    private String keyWords;

    ErrorType(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getKeyWords() {
        return keyWords;
    }


    /**
     * 比较是否相同
     * 由于 enum 类型的值实际上是通过运行期构造出对象来表示的，
     * 所以在 cluster 环境下，每个虚拟机都会构造出一个同义的枚举对象。
     * 因而在做比较操作时候就需要注意，如果直接通过使用等号 ( ‘ == ’ ) 操作符，
     * 这些看似一样的枚举值一定不相等，因为这不是同一个对象实例。
     *
     * @param type
     * @return: boolean
     */
    public boolean match(ErrorType type) {
        if (type == null) {
            return false;
        }
        return this.name().equals(type.name());
    }



//    ****************************
//    exec: source /home/aix/miniconda3/bin/activate aix-task && pip install  --prefer-binary -i http://mirrors.aliyun.com/pypi/simple/ --cache-dir "/home/aix/cache/pip/" tensorflow && source /home/aix/miniconda3/bin/activate aix-task && cd /nfs/mc/docker/plate-recognition && python -u -m recognition.main -name prov_test
//        ****************************
//        17:03:30,048 [INFO] (aix-t4):
//    IpoM8-nAs: Looking in indexes: http://mirrors.aliyun.com/pypi/simple/
//        17:03:30,119 [ERROR] (aix-t1):
//    IpoM8-nAs: WARNING: The repository located at mirrors.aliyun.com is not a trusted or secure host and is being ignored. If this repository is available via HTTPS we recommend you use HTTPS instead, otherwise you may silence this warning and allow it anyway with '--trusted-host mirrors.aliyun.com'.
//        17:03:30,124 [ERROR] (aix-t2):
//    IpoM8-nAs: ERROR: Could not find a version that satisfies the requirement tensorflow (from versions: none)
//17:03:30,129 [ERROR] (aix-t3):
//    IpoM8-nAs: ERROR: No matching distribution found for tensorflow
//17:03:30,134 [INFO] (aix-t4):
//    IpoM8-nAs: resultCode=1








//    IpoM8-nAs:   File "/home/aix/miniconda3/envs/aix-task/lib/python3.6/site-packages/pip/_vendor/urllib3/response.py", line 430, in _error_catcher
//17:38:54,673 [ERROR] (aix-t3):
//    IpoM8-nAs:     raise ReadTimeoutError(self._pool, None, "Read timed out.")
//17:38:54,677 [ERROR] (aix-t4):
//    IpoM8-nAs: pip._vendor.urllib3.exceptions.ReadTimeoutError: HTTPSConnectionPool(host='pypi.tuna.tsinghua.edu.cn', port=443): Read timed out.
//17:38:54,682 [INFO] (aix-t2):
//    IpoM8-nAs: resultCode=2





//
//    IpoM8-nAs: ERROR: Could not install packages due to an EnvironmentError: HTTPSConnectionPool(host='files.pythonhosted.org', port=443): Max retries exceeded with url: /packages/11/df/5a3bba95b4600d5ca7aff072082ef0d9837056dd28cc4e738e7ce88dd8f8/imgaug-0.3.0-py2.py3-none-any.whl
//    (Caused by ConnectTimeoutError(<pip._vendor.urllib3.connection.VerifiedHTTPSConnection object at 0x7fda1f71b630>,
//    'Connection to files.pythonhosted.org timed out. (connect timeout=15)'))
//        22:19:36,435 [INFO] (aix-t1):
//    IpoM8-nAs: resultCode=1






//    2020-04-18 13:03:54,858 [ERROR] (t5): Shell Error :ERROR: Could not find a version that satisfies the requirement imgaug==0.3.0 (from -r /nfs/mc/docker/plate-recognition/requirements.txt (line 1)) (from versions: none)
//        2020-04-18 13:03:54,858 [DEBUG] (t5): SEND:
//    Message{intent=SHELL_ERROR_HELP, value='ERROR: Could not find a version that satisfies the requirement imgaug==0.3.0 (from -r /nfs/mc/docker/plate-recognition/requirements.txt (line 1)) (from versions: none)', token='eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTExMTExMTExMTExMTExMTExMTEifQ.it6iBaBmEkYIkJ49_2dCUL6nSqH7SHTJJ2IpoM8-nAs', customDataMap=null}
//2020-04-18 13:03:54,863 [ERROR] (t5): Shell Error :ERROR: No matching distribution found for imgaug==0.3.0 (from -r /nfs/mc/docker/plate-recognition/requirements.txt (line 1))
//        2020-04-18 13:03:54,864 [DEBUG] (t5): SEND:
//    Message{intent=SHELL_ERROR_HELP, value='ERROR: No matching distribution found for imgaug==0.3.0 (from -r /nfs/mc/docker/plate-recognition/requirements.txt (line 1))', token='eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTExMTExMTExMTExMTExMTExMTEifQ.it6iBaBmEkYIkJ49_2dCUL6nSqH7SHTJJ2IpoM8-nAs', customDataMap=null}
//2020-04-18 13:03:54,875 [INFO] (t2): Shell Finished :1


}
