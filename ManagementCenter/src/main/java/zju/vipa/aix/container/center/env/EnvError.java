package zju.vipa.aix.container.center.env;

import zju.vipa.aix.container.config.ErrorType;

/**
 * @Date: 2020/3/17 22:46
 * @Author: EricMa
 * @Description: 环境配置过程中的error
 */
public class EnvError {
    private ErrorType type;
    private String details;

    /** 可能的修复语句 */
    private String repairCmds;

    public EnvError(ErrorType type, String details, String repairCmds) {
        this.type = type;
        this.details = details;
        this.repairCmds = repairCmds;
    }

    public ErrorType getType() {
        return type;
    }

    public void setType(ErrorType type) {
        this.type = type;
    }

    public String getRepairCmds() {
        return repairCmds;
    }

    public void setRepairCmds(String repairCmds) {
        this.repairCmds = repairCmds;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
