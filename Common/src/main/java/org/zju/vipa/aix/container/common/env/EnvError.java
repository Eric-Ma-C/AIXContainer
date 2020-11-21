package org.zju.vipa.aix.container.common.env;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/3/17 22:46
 * @Author: EricMa
 * @Description: 环境配置过程中的error
 */
public class EnvError {
    private ErrorType type;
    private String details;

    /** 可能的修复语句 */
    private List<String> repairCmdList;

    public EnvError(ErrorType type, String details, String repairCmd) {
        this.type = type;
        this.details = details;
        this.repairCmdList = new ArrayList<>();
        this.repairCmdList.add(repairCmd);
    }

    public EnvError(ErrorType type, String details,  List<String> repairCmdList) {
        this.type = type;
        this.details = details;
        this.repairCmdList = repairCmdList;
    }

    public ErrorType getType() {
        return type;
    }

    public void setType(ErrorType type) {
        this.type = type;
    }

    public List<String> getRepairCmdList() {
        return repairCmdList;
    }

    public void setRepairCmdList(List<String> repairCmdList) {
        this.repairCmdList = repairCmdList;
    }

    @Deprecated
    public void addRepairCmd(String repairCmd) {
        if (repairCmdList==null) {
            repairCmdList=new ArrayList<>();
        }
        repairCmdList.add(repairCmd);
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
