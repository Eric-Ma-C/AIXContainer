package zju.vipa.aix.container.center.env;

/**
 * @Date: 2020/3/17 22:46
 * @Author: EricMa
 * @Description: 环境配置过程中的error
 */
public class EnvError {
    private String type;
    private String details;

    /** 可能的修复语句 */
    private String repairCmds;

    public EnvError(String type, String details, String repairCmds) {
        this.type = type;
        this.details = details;
        this.repairCmds = repairCmds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
