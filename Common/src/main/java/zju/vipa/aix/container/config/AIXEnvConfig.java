package zju.vipa.aix.container.config;

import java.util.Random;

/**
 * @Date: 2020/3/26 18:39
 * @Author: EricMa
 * @Description: python环境配置指令
 */
public class AIXEnvConfig {
    private static final String CONDA_ENV_NAME = "aix-task";
    //    public static final String CONDA_ENV_NAME ="clean_yolo";
    private static final String MINICONDA_DIR = "/home/aix/miniconda3/";
    /**
     * conda源更新指令
     */
//    private static final String CONDA_CREATE_CMD = "conda env create -n " + CONDA_ENV_NAME + " python=3.6.2";
    private static final String CONDA_CREATE_CMD = "conda env create -n " + CONDA_ENV_NAME;
//    private static final String CONDA_CREATE_FROM_FILE_CMD =CONDA_CREATE_CMD + " python=3.6.2";
    private static final String CONDA_CREATE_PIP_CMD = CONDA_CREATE_CMD + " pip -y";

    public static final String UPDATE_CONDA_SOURCE_CMD = "echo \"" + NetworkConfig.DEFAULT_CONDA_SOURCE + "\" > /home/aix/.condarc && conda clean -i";
    public static final String CONDA_ACTIVATE_CMD = "source " + MINICONDA_DIR + "bin/activate " + CONDA_ENV_NAME;


    /**
     * 阿里云
     */
    private static final String PIP_SOURCE_ALIYUN = "https://mirrors.aliyun.com/pypi/simple/";
    /**
     * 中国科技大学
     */
    private static final String PIP_SOURCE_USTC = "https://pypi.mirrors.ustc.edu.cn/simple/";
    /**
     * 豆瓣(douban)
     */
    private static final String PIP_SOURCE_DOUBAN = "https://pypi.douban.com/simple/";
    /**
     * 清华大学
     */
    private static final String PIP_SOURCE_TUNA = "https://pypi.tuna.tsinghua.edu.cn/simple/";

    private static String PIP_SOURCE = PIP_SOURCE_USTC;
    private static final String PIP_CACHE_DIR = "/home/aix/cache/pip/";
    //    private static final String PIP_INSTALL = "pip install  --prefer-binary  --cache-dir \"" + PIP_CACHE_DIR + "\" ";
    private static final String PIP_INSTALL = "pip install  --prefer-binary -i " + PIP_SOURCE + " --cache-dir \"" + PIP_CACHE_DIR + "\" ";


    /**
     * 换一个pip源
     */
    public static void changePipSource() {
        String[] pipUrls = new String[]{PIP_SOURCE_ALIYUN, PIP_SOURCE_USTC, PIP_SOURCE_DOUBAN, PIP_SOURCE_TUNA};
        int newId = (new Random().nextInt(100)) % pipUrls.length;
        PIP_SOURCE = pipUrls[newId];
    }

    /**
     * conda配置创建命令
     */
    public static String getCondaEnvCreateCmds(String codePath) {

        return CONDA_CREATE_CMD + " -f " + codePath + "/environment.yaml";//--json
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
    public static String getStartCmds(String codePath,String modelArgs) {
//        python main.py annotation=下载的annotations文件路径 + 空格 + 数据库中的自定义参数
        return CONDA_ACTIVATE_CMD + " && python " + codePath + "/main.py --annotation "+Config.DATASET_SAVE_PATH+" "+modelArgs;


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
        return CONDA_ACTIVATE_CMD + " && pip uninstall " + moduleName+" -y";
//            " && python " + task.getCodePath() + "/main.py";

    }

}
