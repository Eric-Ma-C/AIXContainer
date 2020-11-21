package org.zju.vipa.aix.container.common.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zju.vipa.aix.container.common.config.AIXEnvConfig;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/3/26 21:03
 * @Author: EricMa
 * @Description: shell错误解析器
 */
public class ErrorParser {
    /**
     *   处理容器报错信息
     * @param token 容器token
     * @param runningCmds 容器正在运行的命令
     * @param errorInfo 容器报错信息
     * @param task 容器任务
     * @return: org.zju.vipa.aix.container.common.env.EnvError
     */
    public static EnvError handle(String token,String runningCmds, String errorInfo, Task task) {

        if (errorInfo == null) {
            return null;
        }
        List<String> repairCmds = new ArrayList<>();
        ErrorType errorType = ErrorType.UNKNOWN;

        ErrorType[] types = ErrorType.values();
        for (ErrorType type : types) {
            //逐一对比，可能会比较慢
            if (errorInfo.contains(type.getKeyWords())) {
                errorType = type;
                break;
            }
        }
        String startCmds = AIXEnvConfig.getStartCmds(task,token);
        switch (errorType) {
            case UNKNOWN:
                /** 可能是一些非关键描述信息，无法提取错误类别 */
                return null;
            case HTTP_RETRY:
                repairCmds.add(runningCmds);
                break;
            case APT_RETRY:
                repairCmds.add(AIXEnvConfig.CHANGE_APT_SOURCE_CMD);
                repairCmds.add(runningCmds);

                break;
            case GCC_NOT_FOUND:
                repairCmds.add("sudo apt-get update && sudo apt-get install gcc -y");
                break;
            case MODULE_NOT_FOUND:
                /** 自动安装一些conda库 */
                String promptName = errorInfo.substring(errorInfo.indexOf("named") + 7, errorInfo.length() - 1);
                String moduleName = getModuleNameByPrompt(promptName);

                repairCmds.add(AIXEnvConfig.getPipInstallCmds(moduleName));
                repairCmds.add(startCmds);
                break;
            case CONDA_PREFIX_ALREADY_EXISTS:
                /** 直接重启 */
                repairCmds.add(startCmds);
                break;
            case CONDA_PREFIX_NOT_FOUND:
                /** 重新安装conda环境 */
                repairCmds.add(AIXEnvConfig.getCondaEnvCreateCmds(task));
                repairCmds.add(task.getPreCmds());
                repairCmds.add(startCmds);
                break;
            case CUDA_OUT_OF_MEMORY:
                /**  内存不够,等一会儿重启任务  */
                repairCmds.add("sleep 30 && " + startCmds);
                break;
            case UNICODE_ENCODE_ERROR:
                /**  编码问题  */
                repairCmds.add(startCmds.replace("python", "PYTHONIOENCODING=utf-8 python")) ;
                break;
            case NUMPY_LEVEL_TOO_HIGH:
                /**  降级安装numpy  */
                repairCmds.add(AIXEnvConfig.getPipUninstallCmds("numpy"));
                repairCmds.add(AIXEnvConfig.getPipInstallCmds("numpy==1.17.0"));
                repairCmds.add(startCmds);

                break;
            case TENSORFLOW_LEVEL_TOO_HIGH:
                /**  降级安装tensorflow  */
                repairCmds.add(AIXEnvConfig.getPipUninstallCmds("tensorflow"));
                repairCmds.add(AIXEnvConfig.getPipInstallCmds("tensorflow-gpu==1.13.1"));
                repairCmds.add(startCmds);

                break;
            case PIP_SOURCE_READ_TIMED_OUT:
            case PIP_SOURCE_NO_MATCHING_DISTRIBUTION:
                /** pip换源 */
                AIXEnvConfig.changePipSource();
                repairCmds.add(runningCmds);
                break;
            case IMPORTERROR_LIBSM_SO_6:
                repairCmds.add("sudo apt-get update && sudo apt-get install -y libsm6 libxext6");
                repairCmds.add(startCmds);

                break;
            case CONDA_SOURCE_304_ERROR:
                /** conda index cache被污染，使用conda clean -i 清理 */
                repairCmds.add("conda clean -i");
                repairCmds.add(startCmds);

                break;
            default:
                /**  其他错误,什么都不做  */
                break;
        }
        Logger logger = LoggerFactory.getLogger(ErrorParser.class);
        logger.info("{}:\n'{}'添加至error列表中，repairCmds={}", token, errorType.name(), repairCmds);

        return new EnvError(errorType, errorInfo, repairCmds);
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
