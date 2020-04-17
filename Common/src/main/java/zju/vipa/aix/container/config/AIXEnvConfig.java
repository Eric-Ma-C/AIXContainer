package zju.vipa.aix.container.config;

/**
 * @Date: 2020/3/26 18:39
 * @Author: EricMa
 * @Description: conda配置
 */
public class AIXEnvConfig {
    private static final String CONDA_ENV_NAME = "aix-task";
    //    public static final String CONDA_ENV_NAME ="clean_yolo";
    private static final String MINICONDA_DIR = "/home/aix/miniconda3/";
    /**
     * conda源更新指令
     */
    private static final String UPDATE_CONDA_SOURCE_CMD = "echo \"" + NetworkConfig.DEFAULT_CONDA_SOURCE + "\" > /home/aix/.condarc && conda clean -i";
    private static final String CONDA_CREATE_CMD = "conda create -n " + CONDA_ENV_NAME + " python=3.6.2";
    private static final String CONDA_CREATE_PIP_CMD = CONDA_CREATE_CMD + " pip -y";
    private static final String CONDA_ACTIVATE_CMD = "source " + MINICONDA_DIR + "bin/activate " + CONDA_ENV_NAME;







    //      #阿里云 http://mirrors.aliyun.com/pypi/simple/
//          #中国科技大学 https://pypi.mirrors.ustc.edu.cn/simple/
//          #豆瓣(douban) http://pypi.douban.com/simple/
//          #清华大学 https://pypi.tuna.tsinghua.edu.cn/simple/
//          #中国科学技术大学 http://pypi.mirrors.ustc.edu.cn/simple/
    private static final String PIP_SOURCE = "http://mirrors.aliyun.com/pypi/simple/";
    private static final String PIP_CACHE_DIR = "/home/aix/cache/pip/";
    private static final String PIP_INSTALL = "pip install  --prefer-binary -i " + PIP_SOURCE + " --cache-dir \"" + PIP_CACHE_DIR + "\" ";





    /**
     * conda配置创建命令
     */
    public static String getCondaEnvCreateCmds(String codePath) {

//        return "conda env create -n " + AIXEnvConfig.CONDA_ENV_NAME + " -f " + codePath + "/environment.yaml";//--json
//        return "conda env create -f " +codePath+"/requirements.txt -n "+ AIXEnvConfig.CONDA_ENV_NAME ;
        return getPipEnvCreateCmds(codePath);
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
    public static String getStartCmds(String codePath) {
//        return "source /home/aix/miniconda3/bin/activate " + AIXEnvConfig.CONDA_ENV_NAME + " && python " + codePath + "/main.py";
        return CONDA_ACTIVATE_CMD + " && cd " + codePath + " && python -u -m recognition.main -name prov_test";
    }


    /**
     * pip补充配置命令,一般在conda配置完成后使用
     */
    public static String getPipInstallCmds(String moduleName) {
        return CONDA_ACTIVATE_CMD + " && " + PIP_INSTALL + moduleName;
//            " && python " + task.getCodePath() + "/main.py";

    }

}
