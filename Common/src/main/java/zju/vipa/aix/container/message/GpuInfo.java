package zju.vipa.aix.container.message;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/3/11 22:06
 * @Author: EricMa
 * @Description: gpu
 */
public class GpuInfo {
    private String nvidiaSmi, driverVersion, cudaVersion;
    private List<Gpu> gpus;
    private List<Process> processes;

    public GpuInfo() {
        gpus=new ArrayList<>();
        processes=new ArrayList<>();
    }

    public static class Gpu{
        String id,name,fan,temp,usage,cap,memUsed,memAll;

        public Gpu() {
        }

        public Gpu(String id, String name, String fan, String temp, String usage, String cap, String memUsed, String memAll) {
            this.id = id;
            this.name = name;
            this.fan = fan;
            this.temp = temp;
            this.usage = usage;
            this.cap = cap;
            this.memUsed = memUsed;
            this.memAll = memAll;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFan() {
            return fan;
        }

        public void setFan(String fan) {
            this.fan = fan;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getUsage() {
            return usage;
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public String getCap() {
            return cap;
        }

        public void setCap(String cap) {
            this.cap = cap;
        }

        public String getMemUsed() {
            return memUsed;
        }

        public void setMemUsed(String memUsed) {
            this.memUsed = memUsed;
        }

        public String getMemAll() {
            return memAll;
        }

        public void setMemAll(String memAll) {
            this.memAll = memAll;
        }
    }
    public static class Process {
        String gpuId,pid,type,processName,memUsed;

        public Process() {
        }

        public Process(String gpuId, String pid, String type, String processName, String memUsed) {
            this.gpuId = gpuId;
            this.pid = pid;
            this.type = type;
            this.processName = processName;
            this.memUsed = memUsed;
        }

        public String getGpuId() {
            return gpuId;
        }

        public void setGpuId(String gpuId) {
            this.gpuId = gpuId;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getProcessName() {
            return processName;
        }

        public void setProcessName(String processName) {
            this.processName = processName;
        }

        public String getMemUsed() {
            return memUsed;
        }

        public void setMemUsed(String memUsed) {
            this.memUsed = memUsed;
        }
    }

    public String getNvidiaSmi() {
        return nvidiaSmi;
    }

    public void setNvidiaSmi(String nvidiaSmi) {
        this.nvidiaSmi = nvidiaSmi;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getCudaVersion() {
        return cudaVersion;
    }

    public void setCudaVersion(String cudaVersion) {
        this.cudaVersion = cudaVersion;
    }

    public List<Gpu> getGpus() {
        return gpus;
    }

    public void addGpu(Gpu gpu) {
        this.gpus.add(gpu);
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void addProcess(Process process) {
        this.processes.add(process);
    }

    public void setGpus(List<Gpu> gpus) {
        this.gpus = gpus;
    }
    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
}
