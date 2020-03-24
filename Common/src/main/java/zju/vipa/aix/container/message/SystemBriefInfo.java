package zju.vipa.aix.container.message;

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
    /** token */
    private String token;

    public SystemBriefInfo(int cpuRate, int ramRate, String token) {
        this.cpuRate = cpuRate;
        this.ramRate = ramRate;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
