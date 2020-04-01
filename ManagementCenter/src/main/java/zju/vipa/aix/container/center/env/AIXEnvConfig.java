package zju.vipa.aix.container.center.env;

import zju.vipa.aix.container.config.NetworkConfig;

/**
 * @Date: 2020/3/26 18:39
 * @Author: EricMa
 * @Description: conda配置
 */
public class AIXEnvConfig {
//    public static final String CONDA_ENV_NAME ="aix-task";
    public static final String CONDA_ENV_NAME ="clean_yolo";

    /**
     * conda源更新指令
     */
    public static String getUpdateCondaSrcCmds() {

        return "echo \"" + NetworkConfig.DEFAULT_CONDA_SOURCE + "\" > /root/.condarc && conda clean -i";

    }
}
