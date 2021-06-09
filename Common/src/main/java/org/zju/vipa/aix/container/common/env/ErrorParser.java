package org.zju.vipa.aix.container.common.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zju.vipa.aix.container.common.config.AIXEnvConfig;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/3/26 21:03
 * @Author: EricMa
 * @Description: shell错误解析器
 */
public class ErrorParser {
    private static Logger logger = LoggerFactory.getLogger(ErrorParser.class);
    /**
     * 最后一次报错时间，用于同一次Error区分
     */
    private static long lastErrorTime;
    /** volatile保证可见性 */
    private volatile static List<KnownErrorRuntime> knownErrorRuntimeList;

    public static void refreshRuntimeErrorList(List<KnownErrorRuntime> errorList){
//        logger.info("refreshRuntimeErrorList:");
//        for (KnownErrorRuntime errorRuntime : errorList) {
//            logger.info("\n"+errorRuntime.toString());
//        }
        knownErrorRuntimeList=errorList;
    }

    /**
     * 处理容器报错信息
     *
     * @param token       容器token
     * @param runningCmds 容器正在运行的命令
     * @param errorInfo   容器报错信息
     * @param task        容器任务
     * @return: org.zju.vipa.aix.container.common.env.EnvError
     */
    public static EnvError handle(String token, String runningCmds, String errorInfo, Task task) {

        if (errorInfo == null) {
            return null;
        }
        List<String> repairCmds = new ArrayList<>();
        StaticErrorType staticErrorType = StaticErrorType.UNKNOWN;
        String errorName=StaticErrorType.UNKNOWN.name();

        StaticErrorType[] types = StaticErrorType.values();
        for (StaticErrorType type : types) {
            //逐一对比，可能会比较慢
            if (errorInfo.contains(type.getKeyWords())) {
                staticErrorType = type;
                errorName=type.name();
                break;
            }
        }
        String startCmds = AIXEnvConfig.getStartCmds(task, token);
        switch (staticErrorType) {
            case UNKNOWN:
                boolean solved=false;
                /** 在KnownErrorRuntime中查询错误 */
                for (KnownErrorRuntime errorRuntime : knownErrorRuntimeList) {
                    //逐一对比，可能会比较慢
                    if (errorInfo.contains(errorRuntime.getKey_words())) {
                        solved=true;
                        errorName=errorRuntime.getName();
                        List<String> cmds = JsonUtils.getList(errorRuntime.getRepair_cmds(), String.class);
                        for (String cmd : cmds) {
                            if ("$RUNNING_CMDS".equals(cmd)){
                                repairCmds.add(runningCmds);
                            }else {
                                repairCmds.add(cmd);
                            }
                        }
                        break;
                    }
                }
                if (solved){
                    break;
                }else {
                    /** 可能是一些非关键描述信息，无法提取错误类别 */
                    return null;
                }
            case HTTP_RETRY:
                repairCmds.add(runningCmds);
                break;
//            case APT_RETRY:
//                repairCmds.add(AIXEnvConfig.getChangeAptSourceCmd());
//                repairCmds.add("sudo apt-get clean && sudo rm -rf /var/lib/apt/lists.old && sudo mv /var/lib/apt/lists /var/lib/apt/lists.old && sudo mkdir -p /var/lib/apt/lists/partial && sudo apt-get clean");
//                repairCmds.add(runningCmds);
//
//                break;
            case CONNECT_TIMEOUT_ERROR:
                repairCmds.add(runningCmds);
                break;
            case CONDA_CREATE_RETRY:
//                if (runningCmds.contains("conda-env create"))
                repairCmds.add(runningCmds);
                break;
            case APT_GPG_ERRER:
//                repairCmds.add("sudo apt-get clean && sudo rm -rf /var/lib/apt/lists.old && sudo mv /var/lib/apt/lists /var/lib/apt/lists.old && sudo mkdir -p /var/lib/apt/lists/partial && sudo apt-get clean");
                /** 添加gpg公钥 */
                repairCmds.add("curl -sL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -");
                repairCmds.add(runningCmds);
                break;
            case APT_CUDA_ERROR:
                repairCmds.add("sudo rm /etc/apt/sources.list.d/cuda.list && sudo rm /etc/apt/sources.list.d/nvidia-ml.list");
                repairCmds.add(runningCmds);
                break;
            case GCC_NOT_FOUND:
                /** 安装gcc g++ */
//                repairCmds.add(AIXEnvConfig.getPipInstallCmds("gcc"));
//                repairCmds.add("sudo aptitude install -y gcc");
//                repairCmds.add("expect -c ' " +
//                    " spawn sudo aptitude install  gcc  \\\n" +
//                    " expect \"Accept this solution?\"  \\\n" +
//                    " send \"n\"  \\\n" +
//                    " expect \"Downgrade\"  \\\n" +
//                    " send \"Y\"  \\\n" +
//                    " expect eof'");
                repairCmds.add("expect -c 'set timeout 300 \n" +
                    " spawn sudo aptitude install  gcc \n" +
                    " expect \"Accept this solution?\"  \n" +
                    " send \"n\\n\" \n" +
                    " expect \"Downgrade the following packages\"  \n" +
                    " send \"Y\\n\"  \n" +
                    " expect \"Do you want to continue?\"  \n" +
                    " send \"Y\\n\"\n" +
                    " expect eof'");
                if (runningCmds.contains("conda-env create")) {
                    repairCmds.add(AIXEnvConfig.CONDA_REMOVE_ALL_CMD);
                }
                repairCmds.add(runningCmds);
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
                repairCmds.add(startCmds.replace("python", "PYTHONIOENCODING=utf-8 python"));
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
                if (runningCmds.startsWith("conda-env create")){
                    repairCmds.add(AIXEnvConfig.CONDA_REMOVE_ALL_CMD);
                }
                repairCmds.add(runningCmds);
                break;
            case IMPORTERROR_LIBSM_SO_6:
//                repairCmds.add("sudo apt-get update && sudo apt-get install -y libsm6 libxext6");
                repairCmds.add("aptitude install -y libsm6 libxext6");
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



        long millis = System.currentTimeMillis();
        if (millis - lastErrorTime < 500) {
            /** 2020-12-02 19:44:23,397  gcc
             *  2020-12-02 19:44:23,575  gcc*/
            /** 500ms内认为是同一个error */
            logger.warn("{}:忽略Error：{}，repairCmds={}", token, errorName, repairCmds);
            return null;
        }
        lastErrorTime = millis;

        logger.info("{}:\n'{}'添加至error列表中，repairCmds={}", token, errorName, repairCmds);

        return new EnvError(errorName, errorInfo, repairCmds);
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
