package org.zju.vipa.aix.container.message;

/**
 * @Date: 2020/3/10 15:27
 * @Author: EricMa
 * @Description: 系统cpu，ram占用，心跳包内容
 */
public class SystemBriefInfo {
    /** cpu */
    private int cpuRate;
    /** memory */
    private int ramRate;
    /** gpu */
//    private int gpuRate;


    public SystemBriefInfo(int cpuRate, int ramRate) {
        this.cpuRate = cpuRate;
        this.ramRate = ramRate;

    }

    public int getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(int cpuRate) {
        this.cpuRate = cpuRate;
    }

    public int getRamRate() {
        return ramRate;
    }

    public void setRamRate(int ramRate) {
        this.ramRate = ramRate;
    }

}
