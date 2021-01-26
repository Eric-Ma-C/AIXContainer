package org.zju.vipa.aix.container.api.entity;

import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;
import org.zju.vipa.aix.container.common.message.GpuInfo;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Date: 2020/12/1 15:26
 * @Author: EricMa
 * @Description: 容器Device信息 view object
 */
public class AixDeviceVO implements Serializable {
    private int id;
    private String deviceName;
    private String createdTime;
    private String lastLogin;


    private String info;
    private String token;
    private int userId;

    private String  driverVersion, cudaVersion;
    private List<GpuInfo.Gpu> gpus;
    private int gpuNum;
    /** 多块GPU算力总和 */
    private float calPowerSum;
    /** 多块GPU显存总和 单位MB */
    private int gpuMemSum;

    private AixDeviceVO() {
    }

    public AixDeviceVO(AixDevice device) {
        if (device==null){
            return;
        }
        id=device.getId();
        deviceName =device.getDeviceName();
        createdTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(device.getCreatedTime());
        lastLogin =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(device.getLastLogin());
        info=device.getInfo();
        token=device.getToken();
        userId =device.getUserId();

        GpuInfo gpuInfo= JsonUtils.parseObject(device.getDetail(),GpuInfo.class);
        if (gpuInfo != null) {
            driverVersion=gpuInfo.getDriverVersion();
            cudaVersion=gpuInfo.getCudaVersion();
            gpuNum=gpuInfo.getGpuNum();
            gpus=gpuInfo.getGpus();
            calPowerSum=0.0f;
            gpuMemSum=0;
            for (GpuInfo.Gpu gpu : gpus) {
                calPowerSum+=gpu.getCalPower();
                //匹配非数字字符，然后全部替换为空字符
                String memStr = Pattern.compile("[^0-9]").matcher(gpu.getMemAll()).replaceAll("");
                gpuMemSum+=Integer.valueOf(memStr);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
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

    public List<GpuInfo.Gpu> getGpus() {
        return gpus;
    }

    public void setGpus(List<GpuInfo.Gpu> gpus) {
        this.gpus = gpus;
    }

    public int getGpuNum() {
        return gpuNum;
    }

    public void setGpuNum(int gpuNum) {
        this.gpuNum = gpuNum;
    }

    public float getCalPowerSum() {
        return calPowerSum;
    }

    public void setCalPowerSum(float calPowerSum) {
        this.calPowerSum = calPowerSum;
    }

    public int getGpuMemSum() {
        return gpuMemSum;
    }

    public void setGpuMemSum(int gpuMemSum) {
        this.gpuMemSum = gpuMemSum;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
