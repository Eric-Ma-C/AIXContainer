package org.zju.vipa.aix.container.center.util;

import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2020/12/22 13:54
 * @Author: EricMa
 * @Description: gpu算力
 */
public class GpuUtils {
//    private static final Logger logger = LoggerFactory.getLogger(GpuUtils.class);

    private class GpuPower{
        String name;
        float calPower;
    }

    private static List<GpuPower> gpuPowerList = null;
    private static Map<String,Float> gpuPowerMap = new ConcurrentHashMap<>();

    static {

        Path path = new File("/nfs2/aix-container/server/gpus.txt").toPath();
        LogUtils.debug("gpus.txt path={}",path);
        byte[] allBytes = new byte[0];
        try {
            allBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }
        gpuPowerList = JsonUtils.getList(new String(allBytes), GpuPower.class);
        for (GpuPower gpuPower : gpuPowerList) {
            gpuPowerMap.put(gpuPower.name,gpuPower.calPower);
        }
        LogUtils.info("初始化{}种GPU算力信息成功",gpuPowerList.size());

    }
    public static float getGpuPower(String name){
        if (gpuPowerMap.containsKey(name)) {
            return gpuPowerMap.get(name);
        }else {
            return 0.0f;
        }

    }
}
