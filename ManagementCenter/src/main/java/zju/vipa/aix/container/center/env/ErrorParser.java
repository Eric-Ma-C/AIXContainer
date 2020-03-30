package zju.vipa.aix.container.center.env;

import zju.vipa.aix.container.center.db.entity.Task;
import zju.vipa.aix.container.utils.LogUtils;

/**
 * @Date: 2020/3/26 21:03
 * @Author: EricMa
 * @Description: shell错误解析器
 */
public class ErrorParser {
    /**
     * 任务运行报错，一般可以通过pip安装对应的库
     */
    public static final String MODULE_NOT_FOUND_ERROR = "ModuleNotFoundError: No module named";
    /**
     * 已有同名conda环境
     */
    public static final String CONDA_PREFIX_ALREADY_EXISTS = "CondaValueError: prefix already exists";
    /**
     * conda环境未找到
     */
    public static final String CONDA_PREFIX_NOT_FOUND = "Could not find conda environment";



    public static EnvError handle(String token,String value, Task task) {
        if (value == null) {
            return null;
        }
        String repairCmds = null;
        String type=null;

        if (value.startsWith(MODULE_NOT_FOUND_ERROR)) {
            type=MODULE_NOT_FOUND_ERROR;

            /** 自动安装一些conda库 */
            String moduleName = value.substring(value.indexOf("named") + 7, value.length() - 1);
            repairCmds = task.getPipComplementCmds(moduleName)+" && "+task.getStartCmds();

        } else if (value.contains(CONDA_PREFIX_ALREADY_EXISTS)) {
            type=CONDA_PREFIX_ALREADY_EXISTS;
            repairCmds = task.getStartCmds();

        } else if (value.contains(CONDA_PREFIX_NOT_FOUND)) {
            type=CONDA_PREFIX_NOT_FOUND;
            repairCmds = task.getCondaEnvCreateCmds()+" && "+task.getStartCmds();


        } else {
            /**  其他错误,什么都不做  */
            return null;
        }

        LogUtils.info(token, "'"+type+"'添加至error列表中，repairCmds=" + repairCmds);
        return new EnvError(type,value,repairCmds);
    }
}
