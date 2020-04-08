package zju.vipa.aix.container.center.env;

import zju.vipa.aix.container.config.NetworkConfig;

/**
 * @Date: 2020/3/26 18:39
 * @Author: EricMa
 * @Description: conda配置
 */
public class AIXEnvConfig {
    public static final String CONDA_ENV_NAME = "aix-task";
    //    public static final String CONDA_ENV_NAME ="clean_yolo";

    /**
     * conda源更新指令
     */
    public static final String UPDATE_CONDA_SOURCE_CMD = "echo \"" + NetworkConfig.DEFAULT_CONDA_SOURCE + "\" > /root/.condarc && conda clean -i";
    public static final String CONDA_ACTIVATE_CMD = "source /root/miniconda3/bin/activate " + CONDA_ENV_NAME;
    public static final String CONDA_INSTALL_PIP_CMD = "conda create -n " + AIXEnvConfig.CONDA_ENV_NAME + " python=3.6.2 pip -y";



    /**
     * pip根据文件安装指令
     */
    public static String getPipFileInstallCmds(String codePath) {
        return "pip install  --prefer-binary --cache-dir \"/root/cache/pip/\" -r " + codePath + "/requirements.txt";
    }
}
