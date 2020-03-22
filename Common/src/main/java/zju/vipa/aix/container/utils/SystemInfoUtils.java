package zju.vipa.aix.container.utils;

import com.sun.management.OperatingSystemMXBean;
import zju.vipa.aix.container.message.GpuInfo;
import zju.vipa.aix.container.message.SystemBriefInfo;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

/**
 * @Date: 2020/3/10 14:30
 * @Author: EricMa
 * @Description: 获取linux系统（容器）cpu,ram,gpu信息
 * todo: gpu info
 */
public class SystemInfoUtils {

    private static OperatingSystemMXBean getOsBean() {
        return ManagementFactory.getPlatformMXBean(
            OperatingSystemMXBean.class);
    }

    public static double getCpuRate() {
        OperatingSystemMXBean osBean = getOsBean();
        // What % CPU load this current JVM is taking, from 0.0-1.0
        double jvmCpuLoad = osBean.getProcessCpuLoad();

        // What % load the overall system is at, from 0.0-1.0
        double systemCpuLoad = osBean.getSystemCpuLoad();

        /** load average per cpu */
        double cpuRate = osBean.getSystemLoadAverage() / osBean.getAvailableProcessors();

        return cpuRate;

    }

    public static double getRamRate() {
        OperatingSystemMXBean osBean = getOsBean();
        /** memory */
        double ramRate = osBean.getFreePhysicalMemorySize() / osBean.getTotalPhysicalMemorySize();
        return ramRate;

    }

    public static double getDiskRate(String path) {
        File file;
        if (path == null || "".equals(path)) {
            file = new File("/");
        } else {
            file = new File(path);
        }
        /** disk space */
        double diskRate = file.getUsableSpace() / file.getTotalSpace();
        return diskRate;
    }

    public static GpuInfo getGpuInfo() {
        ShellUtils.CommandResult result = ShellUtils.execCommand("nvidia-smi");
        if (result.result != 0) {
            LogUtils.error(result.errorMsg);
            return null;
        }
        List<String> info = Arrays.asList(result.responseMsg.split("\\s+"));

        GpuInfo gpuInfo = new GpuInfo();
        gpuInfo.setNvidiaSmi(info.get(info.indexOf("NVIDIA-SMI") + 1));
        gpuInfo.setDriverVersion(info.get(info.indexOf("Driver") + 2));
        gpuInfo.setCudaVersion(info.get(info.indexOf("CUDA") + 2));

        int gpuBegin = info.indexOf("|===============================+======================+======================|") + 2;
        int processBegin = info.indexOf("Processes:");
        int count = (processBegin - gpuBegin) / 24;
        for (int i = 0; i < count; i++) {
            gpuInfo.addGpu(new GpuInfo.Gpu(
                info.get(gpuBegin),
                info.get(gpuBegin + 1) + " " + info.get(gpuBegin + 2),
                info.get(gpuBegin + 11),
                info.get(gpuBegin + 12),
                info.get(gpuBegin + 14),
                info.get(gpuBegin + 16),
                info.get(gpuBegin + 18),
                info.get(gpuBegin + 20)));

            gpuBegin += 27;//todo confirm
        }

        processBegin += 14;
        count = (info.size() - processBegin) / 6;
        for (int i = 0; i < count; i++) {
            gpuInfo.addProcess(new GpuInfo.Process(
                info.get(processBegin),
                info.get(processBegin + 1),
                info.get(processBegin + 2),
                info.get(processBegin + 3),
                info.get(processBegin + 4)
            ));
            processBegin += 7;
        }

        return gpuInfo;

    }

    public static SystemBriefInfo getSystemBriefInfo() {
        OperatingSystemMXBean osBean = getOsBean();
        /** cpu */
        int cpuRate = (int) (osBean.getSystemLoadAverage() / osBean.getAvailableProcessors()*100);
        /** memory */
        int ramRate = (int) (osBean.getFreePhysicalMemorySize() * 1.0 / osBean.getTotalPhysicalMemorySize()*100);

        return new SystemBriefInfo(cpuRate, ramRate);
    }

}
