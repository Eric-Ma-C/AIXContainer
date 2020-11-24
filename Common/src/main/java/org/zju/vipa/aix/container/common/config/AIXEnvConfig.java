package org.zju.vipa.aix.container.common.config;

import org.zju.vipa.aix.container.common.db.entity.aix.Task;
import org.zju.vipa.aix.container.common.env.AptSource;
import org.zju.vipa.aix.container.common.env.PipSource;

/**
 * @Date: 2020/3/26 18:39
 * @Author: EricMa
 * @Description: python环境配置指令
 */
public class AIXEnvConfig {
    private static final String CONDA_ENV_NAME = "aix-task";
    //    public static final String CONDA_ENV_NAME ="clean_yolo";
    private static final String CONDA_DIR = "/home/aix/anaconda3/";
    /**
     * conda源更新指令
     */
//    private static final String CONDA_CREATE_CMD = "conda env create -n " + CONDA_ENV_NAME + " python=3.6.2";
    private static final String CONDA_CREATE_CMD = "conda-env create -n " + CONDA_ENV_NAME;
    public static final String CONDA_REMOVE_ALL_CMD = "conda remove -n " + CONDA_ENV_NAME + " --all -y";
    //    private static final String CONDA_CREATE_FROM_FILE_CMD =CONDA_CREATE_CMD + " python=3.6.2";
    private static final String CONDA_CREATE_PIP_CMD = CONDA_CREATE_CMD + " pip -y";

    public static final String UPDATE_CONDA_SOURCE_CMD = "echo \"" + Sources.CONDA_SOURCE_TUNA + "\" > /home/aix/.condarc && conda clean -i";
    public static final String CONDA_ACTIVATE_CMD = "source " + CONDA_DIR + "bin/activate " + CONDA_ENV_NAME;


    private static String PIP_SOURCE = PipSource.defaultUrl();
    private static final String PIP_CACHE_DIR = "/home/aix/cache/pip/";
    //    private static final String PIP_INSTALL = "pip install  --prefer-binary  --cache-dir \"" + PIP_CACHE_DIR + "\" ";
    private static final String PIP_INSTALL = "pip install  --prefer-binary -i " + PIP_SOURCE + " --cache-dir \"" + PIP_CACHE_DIR + "\" ";


    /**
     * 换一个pip源
     */
    public static void changePipSource() {
        PIP_SOURCE = PipSource.nextUrl();
    }

    /**
     * 换一个pip源
     */
    public static String getChangeAptSourceCmd() {
        return "echo \"" + AptSource.nextUrl() + "\" > /etc/apt/sources.list";
    }

//    /**
//     * 换conda源，需要重写客户端的conda配置文件
//     */
//    public static void changeCondaSource() {
//
//        condaSourceId += new Random().nextInt(pipUrls.length-1);
//        pipSourceId %= pipUrls.length;
//        PIP_SOURCE = pipUrls[pipSourceId];
//    }

    /**
     * conda配置创建命令
     */
    public static String getCondaEnvCreateCmds(Task task) {
        String codePath = task.getCodePath();
        while (codePath.endsWith("/")) {
            codePath = codePath.substring(0, codePath.length() - 1);
        }

        return CONDA_CREATE_CMD + " -f " + codePath + "/environment.yml";


//        return getPipEnvCreateCmds(codePath);
    }

    /**
     * pip配置创建命令
     */
    public static String getPipEnvCreateCmds(String codePath) {

        return CONDA_CREATE_PIP_CMD + "  &&  " + CONDA_ACTIVATE_CMD + " && " + getPipEnvFileInstallCmds(codePath);
    }


    /**
     * pip根据文件安装指令
     */
    public static String getPipEnvFileInstallCmds(String codePath) {
        return PIP_INSTALL + " -r " + codePath + "/requirements.txt";
    }


    /**
     * 启动命令
     */
    public static String getStartCmds(String codePath, String modelArgs, String deviceToken) {
//        python main.py annotation=下载的annotations文件路径 + 空格 + 数据库中的自定义参数
        /** UniversalModel */
        return CONDA_ACTIVATE_CMD + " && cd " + codePath + " && python main.py " + modelArgs + " --device_token=" + deviceToken;

    }

        /**
         * (重新)启动命令
         */
    public static String getStartCmds(Task task, String deviceToken) {
//        python main.py annotation=下载的annotations文件路径 + 空格 + 数据库中的自定义参数


        /** UniversalModel */
        return CONDA_ACTIVATE_CMD + " && cd " + task.getCodePath() +
            " && python main.py " + task.getModelArgs() + " --device_token=" + deviceToken;


        /** yolo */
//        return CONDA_ACTIVATE_CMD + " && python " + codePath + "/main.py --annotation="+Config.DATASET_SAVE_PATH+" "+modelArgs;


        /** DeepLabv3+ */
//        return CONDA_ACTIVATE_CMD + " && cd " + codePath + " && source train.sh" ;


//        return CONDA_ACTIVATE_CMD + " && python " + codePath + "/main.py --annotation "+Config.DATASET_SAVE_PATH+" --root_path "+Config.MODEL_UNZIP_PATH+" "+modelArgs;


//        return CONDA_ACTIVATE_CMD + " && cd " + codePath + " && python -u -m recognition.main -name prov_test";
    }


    /**
     * pip补充配置命令,一般在conda配置完成后使用
     */
    public static String getPipInstallCmds(String moduleName) {
        return CONDA_ACTIVATE_CMD + " && " + PIP_INSTALL + moduleName;
//            " && python " + task.getCodePath() + "/main.py";

    }

    /**
     * pip拆卸命令
     */
    public static String getPipUninstallCmds(String moduleName) {
        return CONDA_ACTIVATE_CMD + " && pip uninstall " + moduleName + " -y";
//            " && python " + task.getCodePath() + "/main.py";

    }

}
