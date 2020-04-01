package zju.vipa.aix.container.center.env;

import zju.vipa.aix.container.center.db.entity.Task;
import zju.vipa.aix.container.center.util.LogUtils;

/**
 * @Date: 2020/3/26 21:03
 * @Author: EricMa
 * @Description: shell错误解析器
 */
public class ErrorParser {
//    /**
//     * 任务运行报错，一般可以通过pip安装对应的库
//     */
//    public static final String MODULE_NOT_FOUND_ERROR = "ModuleNotFoundError: No module named";
//    /**
//     * 已有同名conda环境
//     */
//    public static final String CONDA_PREFIX_ALREADY_EXISTS = "CondaValueError: prefix already exists";
//    /**
//     * conda环境未找到
//     */
//    public static final String CONDA_PREFIX_NOT_FOUND = "Could not find conda environment";
//    /**
//     * cuda内存耗尽
//     */
//    public static final String CUDA_OUT_OF_MEMORY = "RuntimeError: CUDA out of memory";



    public static EnvError handle(String token,String value, Task task) {
        if (value == null) {
            return null;
        }
        String repairCmds = null;
        ErrorType errorType=ErrorType.UNKNOWN;

        ErrorType[] types=ErrorType.values();
        for (ErrorType type : types) {
            if (value.contains(type.getKeyWords())){
                errorType=type;
                break;
            }
        }

        switch (errorType){
            case UNKNOWN:
                /** 可能是一些非关键描述信息，无法提取错误类别 */
                return null;
            case MODULE_NOT_FOUND:
                /** 自动安装一些conda库 */
                String moduleName = value.substring(value.indexOf("named") + 7, value.length() - 1);
                repairCmds = task.getPipComplementCmds(moduleName)+" && "+task.getStartCmds();
                break;
            case CONDA_PREFIX_ALREADY_EXISTS:
                /** 直接重启 */
                repairCmds = task.getStartCmds();
                break;
            case CONDA_PREFIX_NOT_FOUND:
                /** 重新安装conda环境 */
                repairCmds = task.getCondaEnvCreateCmds()+" && "+task.getStartCmds();
                break;
            case CUDA_OUT_OF_MEMORY:
                /**  内存不够,等一会儿重启任务  */
                repairCmds = "sleep 30 && "+task.getStartCmds();
                break;
            default:
                /**  其他错误,什么都不做  */
                break;
        }

        LogUtils.info(token, "'"+errorType.name()+"'添加至error列表中，repairCmds=" + repairCmds);

        return new EnvError(errorType,value,repairCmds);
    }
}
