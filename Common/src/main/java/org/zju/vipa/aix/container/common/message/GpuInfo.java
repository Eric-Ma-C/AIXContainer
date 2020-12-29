package org.zju.vipa.aix.container.common.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/3/11 22:06
 * @Author: EricMa
 * @Description: gpu
 */
public class GpuInfo implements Serializable {
    private String driverVersion, cudaVersion;
    private int gpuNum;
    private List<Gpu> gpus;


    public GpuInfo() {
        gpus=new ArrayList<>();

    }

    public static GpuInfo unknownGpuInfo(){
        GpuInfo unknownInfo = new GpuInfo();
        unknownInfo.setGpuNum(0);
        unknownInfo.setCudaVersion("unknown");
        unknownInfo.setDriverVersion("unknown");
        return unknownInfo;
    }

    public static class Gpu implements Serializable{
        String id,name, temperature, powerDraw, powerLimit,memUsed,memAll;
        float calPower;//算力
        private List<Process> processes;

        public Gpu() {

        }

        public Gpu(String id, String name, String temperature, String powerDraw, String powerLimit, String memUsed, String memAll) {
            this.id = id;
            this.name = name;
            this.temperature = temperature;
            this.powerDraw = powerDraw;
            this.powerLimit = powerLimit;
            this.memUsed = memUsed;
            this.memAll = memAll;
            this.processes=new ArrayList<>();
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

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getPowerDraw() {
            return powerDraw;
        }

        public void setPowerDraw(String powerDraw) {
            this.powerDraw = powerDraw;
        }

        public String getPowerLimit() {
            return powerLimit;
        }

        public void setPowerLimit(String powerLimit) {
            this.powerLimit = powerLimit;
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

        public List<Process> getProcesses() {
            return processes;
        }

        public void setProcesses(List<Process> processes) {
            this.processes = processes;
        }

        public void addProcess(Process process) {
            this.processes.add(process);
        }

        public float getCalPower() {
            return calPower;
        }

        public void setCalPower(float calPower) {
            this.calPower = calPower;
        }
    }
    public static class Process implements Serializable{
        String pid,type,processName,memUsed;

        public Process() {
        }

        public Process(String pid, String type, String processName, String memUsed) {

            this.pid = pid;
            this.type = type;
            this.processName = processName;
            this.memUsed = memUsed;
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



    public void setGpus(List<Gpu> gpus) {
        this.gpus = gpus;
    }

    public int getGpuNum() {
        return gpuNum;
    }

    public void setGpuNum(int gpuNum) {
        this.gpuNum = gpuNum;
    }
}
