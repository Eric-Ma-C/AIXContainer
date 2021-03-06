package org.zju.vipa.aix.container.center;

import org.zju.vipa.aix.container.api.entity.RunningClient;
import org.zju.vipa.aix.container.api.entity.TaskBriefInfo;
import org.zju.vipa.aix.container.center.db.DbManager;
import org.zju.vipa.aix.container.center.dubbo.RpcServer;
import org.zju.vipa.aix.container.center.netty.NettyTcpServer;
import org.zju.vipa.aix.container.center.network.SocketServer;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.JwtUtils;
import org.zju.vipa.aix.container.common.config.NetworkConfig;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.message.GpuInfo;
import org.zju.vipa.aix.container.common.utils.JsonUtils;
import org.zju.vipa.aix.container.common.utils.TimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    /**
     * 已连接客户端列表
     */
    private Map<String, RunningClient> clientMap;

    private static class ManagementCenterHolder {
        private static final ManagementCenter INSTANCE = new ManagementCenter();
    }

    private ManagementCenter() {
        if (ManagementCenterHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }
        init();
    }

    public static ManagementCenter getInstance() {
        return ManagementCenterHolder.INSTANCE;
    }

    private void init() {
        cachedTokenMap = new ConcurrentHashMap<>(20);
        clientMap = new ConcurrentHashMap<>(20);
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
            /** todo 目前没有去数据库检查token   id = DbManager.getInstance().getClientIdByToken(token); */
            id = JwtUtils.decodeClinetIdByToken(token);
            if (id != null) {
                /** 新设备接入 */
                cachedTokenMap.put(token, id);
                clientMap.put(token, new RunningClient(id, token, TimeUtils.getCurrentTimeStr()));
            }
        }

        return id;
    }

    public void updateGpuInfo(String token, GpuInfo info) {
        RunningClient client = clientMap.get(token);
        client.setGpuInfo(info);
        clientMap.put(token, client);

        DbManager.getInstance().updateDeviceGpuDetailById(cachedTokenMap.get(token), JsonUtils.toJSONString(info));
    }

    public void updateRunningCmds(String token, String cmds) {
        RunningClient client = clientMap.get(token);
        client.setRunningCmds(cmds);
        clientMap.put(token, client);
    }

    public void updateTaskBriefInfo(String token, TaskBriefInfo briefInfo) {
        RunningClient client = clientMap.get(token);
        client.setTaskBriefInfo(briefInfo);
        clientMap.put(token, client);
    }

    /**
     * 获取已连接容器列表
     */
    public List<RunningClient> getClientsList() {
        List<RunningClient> list = new ArrayList<>(clientMap.values());
        return list;
    }

    public static void main(String[] args) {
        System.out.println("Starting AIX Container Manager...");


        /** 处理主线程的未捕获异常 */
        ExceptionUtils.setDefaultUncaughtExceptionHandler();
        /** dubbo */
        RpcServer.getInstance().start();


        /** 启动tcp服务 */
        if (NetworkConfig.SERVER_USE_NETTY) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Starting Netty Server...");
                    /**  启动Netty tcp服务器 */
                    NettyTcpServer.start();
                }
            }).start();
        } else {
            try {
                SocketServer.getInstance().start();
            } catch (IOException e) {
                ExceptionUtils.handle(e);
            }
        }


    }
}
