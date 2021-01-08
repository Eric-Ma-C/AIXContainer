package org.zju.vipa.aix.container.center.util;

import org.zju.vipa.aix.container.center.log.LogUtils;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
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

    public static class GpuPower{
        String name;
        float calPower;

        public GpuPower() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getCalPower() {
            return calPower;
        }

        public void setCalPower(float calPower) {
            this.calPower = calPower;
        }
    }

    private static List<GpuPower> gpuPowerList = null;
    private static Map<String,Float> gpuPowerMap = new ConcurrentHashMap<>();

    static {
        try (InputStream input = GpuUtils.class.getResourceAsStream("/gpus.txt")) {
            if (input != null) {
                byte b[] = new byte[102400];
                int len = 0;
                int temp = 0; //全部读取的内容都使用temp接收
                while ((temp = input.read()) != -1) { //当没有读取完时，继续读取
                    b[len] = (byte) temp;
                    len++;
                }
                input.close();
                String str = new String(b, 0, len);
                gpuPowerList = JsonUtils.getList(str, GpuPower.class);
                for (GpuPower gpuPower : gpuPowerList) {
                    gpuPowerMap.put(gpuPower.name,gpuPower.calPower);
                }
                LogUtils.info("初始化{}种GPU算力信息成功",gpuPowerList.size());
            }
        } catch (IOException e) {
            ExceptionUtils.handle(e);
        }

//        Path path = new File("/gpus.txt").toPath();
//        byte[] allBytes = new byte[0];
//        try {
//            allBytes = Files.readAllBytes(path);
//        } catch (IOException e) {
//            LogUtils.error(e.getMessage());
//        }


    }
    public static float getGpuPower(String name){
//        return 0.0f;
        if (gpuPowerMap.containsKey(name)) {
            return gpuPowerMap.get(name);
        }else {
            return 0.0f;
        }

    }
}
