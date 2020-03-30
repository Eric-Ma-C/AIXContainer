package zju.vipa.aix.container.center;

import zju.vipa.aix.container.center.network.CenterTcpServer;
import zju.vipa.aix.container.config.NetworkConfig;
import zju.vipa.aix.container.utils.ExceptionUtils;
import zju.vipa.aix.container.utils.JwtUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2020/1/9 20:05
 * @Author: EricMa
 * @Description: aix容器管理平台 启动入口
 */
public class ManagementCenter {
    /**
     * 缓存已经连接到平台的容器token，避免反复查询数据库
     */
    private Map<String, String> cachedTokenMap;

    private static class ManagementCenterHolder {
        private static final ManagementCenter INSTANCE = new ManagementCenter();
    }

    private ManagementCenter() {
        init();
    }

    public static ManagementCenter getInstance() {
        return ManagementCenterHolder.INSTANCE;
    }

    private void init() {
        cachedTokenMap = new ConcurrentHashMap<>(20);
    }


    /**
     * 根据token获取容器id
     *
     * @param token
     * @return: java.lang.String 返回null代表数据库无此设备
     */
    public String getIdByToken(String token) {
        String id = cachedTokenMap.get(token);
        if (id == null) {
            /** todo 目前没有去数据库检查token */
//            id = DbManager.getInstance().getClientIdByToken(token);

            id= JwtUtil.decodeClinetIdByToken(token);
            if (id != null) {
                cachedTokenMap.put(token, id);
            }
        }


        return id;
    }

    public static void main(String[] args) {
        try {
            CenterTcpServer.getInstance().start(NetworkConfig.SERVER_PORT);
        } catch (IOException e) {
            ExceptionUtils.handle(e);
        }
    }
}
