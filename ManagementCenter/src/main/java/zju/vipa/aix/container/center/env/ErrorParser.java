package zju.vipa.aix.container.center.env;

import zju.vipa.aix.container.center.db.entity.Task;
import zju.vipa.aix.container.center.util.LogUtils;
import zju.vipa.aix.container.config.AIXEnvConfig;

/**
 * @Date: 2020/3/26 21:03
 * @Author: EricMa
 * @Description: shell错误解析器
 */
public class ErrorParser {

    public static EnvError handle(String token, String value, Task task) {
        if (value == null) {
            return null;
        }
        String repairCmds = null;
        ErrorType errorType = ErrorType.UNKNOWN;

        ErrorType[] types = ErrorType.values();
        for (ErrorType type : types) {
            if (value.contains(type.getKeyWords())) {
                errorType = type;
                break;
            }
        }
        String startCmds = AIXEnvConfig.getStartCmds(task.getCodePath());
        switch (errorType) {
            case UNKNOWN:
                /** 可能是一些非关键描述信息，无法提取错误类别 */
                return null;
            case MODULE_NOT_FOUND:
                /** 自动安装一些conda库 */
                String promptName = value.substring(value.indexOf("named") + 7, value.length() - 1);
                String moduleName = getModuleNameByPrompt(promptName);

                repairCmds = AIXEnvConfig.getPipInstallCmds(moduleName) + " && " +startCmds;
                break;
            case CONDA_PREFIX_ALREADY_EXISTS:
                /** 直接重启 */
                repairCmds = startCmds;
                break;
            case CONDA_PREFIX_NOT_FOUND:
                /** 重新安装conda环境 */
                repairCmds = AIXEnvConfig.getCondaEnvCreateCmds(task.getCodePath()) + " && " + startCmds;
                break;
            case CUDA_OUT_OF_MEMORY:
                /**  内存不够,等一会儿重启任务  */
                repairCmds = "sleep 30 && " + startCmds;
                break;
            case UNICODE_ENCODE_ERROR:
                /**  编码问题  */
                repairCmds = startCmds.replace("python", "PYTHONIOENCODING=utf-8 python");
                break;
            case NUMPY_RANDOM_HAS_NO_ATTRIBUTE_BIT_GENERATOR:
                /**  降级安装numpy  */
                repairCmds = AIXEnvConfig.CONDA_ACTIVATE_CMD + " && pip uninstall numpy -y && pip install -U numpy==1.17.0 && " + startCmds;
                break;
            default:
                /**  其他错误,什么都不做  */
                break;
        }

        LogUtils.info(token, "'" + errorType.name() + "'添加至error列表中，repairCmds=" + repairCmds);

        return new EnvError(errorType, value, repairCmds);
    }


    /**
     * 提示ModuleNotFoundError: No module named xxx
     * 有时候包名不一致
     */
    private static String getModuleNameByPrompt(String promptName) {
        String moduleName = promptName;
        if ("cv2".equals(moduleName)) {
            moduleName = "opencv-python";
        }

        return moduleName;
    }
}
