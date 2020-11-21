package org.zju.vipa.aix.container.center;

import org.zju.vipa.aix.container.api.entity.RunningClient;
import org.zju.vipa.aix.container.api.entity.TaskBriefInfo;
import org.zju.vipa.aix.container.center.db.DbManager;
import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;
import org.zju.vipa.aix.container.center.dubbo.RpcServer;
import org.zju.vipa.aix.container.center.netty.NettyTcpServer;
import org.zju.vipa.aix.container.center.network.SocketServer;
import org.zju.vipa.aix.container.center.task.TaskManager;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.LogUtils;
import org.zju.vipa.aix.container.common.config.ClientConfig;
import org.zju.vipa.aix.container.common.config.NetworkConfig;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.message.GpuInfo;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2020/1/9 20:05
 * @Author: EricMa
 * @Description: aix容器管理平台 启动入口
 */
public class ManagementCenter {

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
        clientMap = new ConcurrentHashMap<>(20);

        new Timer("RemoveDeadClients Timer").schedule(new TimerTask() {
            @Override
            public void run() {
                removeDeadClients();
            }
        }, 2000, 30000);

    }

    /** 获取容器正在运行的命令 */
    public String getRunningCmdsByToken(String token){
        RunningClient client = clientMap.get(token);
        if (client != null) {
            return client.getRunningCmds();
        }else {
            return null;
        }
    }


    /**
     * 根据token获取容器id
     *
     * @param token
     * @return: java.lang.String 返回""代表数据库无此设备
     */
    public String getIdByToken(String token) {
        RunningClient client = clientMap.get(token);
        String id = "";
        AixDevice device;
        if (client == null) {
            /** 去数据库检查token   */
            device = DbManager.getInstance().getClientByToken(token);

//            id = JwtUtils.decodeClinetIdByToken(token);
            if (device != null) {
                id= String.valueOf(device.getId());
                /** 新设备接入 */
//                clientMap.put(token, new RunningClient(id, token, TimeUtils.getCurrentTimeStr()));
                clientMap.put(token, new RunningClient(device));
            }
        }else {
            id=client.getId();
        }

        LogUtils.debug("getIdByToken({})={}",token,id);
        return id;
    }

    public void updateGpuInfo(String token, GpuInfo info) {
        RunningClient client = clientMap.get(token);
        if (client == null) {
            return;
        }
        client.setGpuInfo(info);
        /** 更新心跳时间 */
        client.setLastHeartbeat(System.currentTimeMillis());
        DbManager.getInstance().updateDeviceGpuDetailById(client.getId(), JsonUtils.toJSONString(info));
    }

    public void updateRunningCmds(String token, String cmds) {
        RunningClient client = clientMap.get(token);
        if (client == null) {
            return;
        }
        client.setRunningCmds(cmds);
    }

    public void updateLatestError(String token, String error) {
        RunningClient client = clientMap.get(token);
        if (client == null) {
            return;
        }
        client.addLatestErrors(error);

        LogUtils.debug("updateLatestError={}",client.getLatestErrors());
    }

    public void updateTaskBriefInfo(String token, TaskBriefInfo briefInfo) {
        RunningClient client = clientMap.get(token);
        if (client == null) {
            return;
        }
        client.setTaskBriefInfo(briefInfo);
    }

    /**
     * 删除超时未显示心跳的容器
     */
    private void removeDeadClients() {
        LogUtils.info("Remove Dead Clients...");
        Iterator<Map.Entry<String, RunningClient>> iterator = clientMap.entrySet().iterator();
        Map.Entry<String, RunningClient> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            RunningClient client=entry.getValue();
            if (System.currentTimeMillis() - client.getLastHeartbeat() > ClientConfig.HEARTBEATS_INTERVAL_MS * 5) {

                LogUtils.debug("\n******* Remove Dead Client: System.currentTimeMillis()={} LastHeartbeat()={} *******\n", System.currentTimeMillis(), entry.getValue().getLastHeartbeat());

                /** 超过心跳间隔时间5倍未收到,认为client已离线 */
                LogUtils.info("\n******* Remove Dead Client: {} *******\n", client.getToken());

                /** 删除TaskManger中的容器信息  */
                TaskManager.getInstance().removeDeadClient(client.getToken());
                /** 从在线容器列表中删除记录 */
                iterator.remove();
            }
        }
    }

    /**
     * 获取已连接容器列表
     */
    public List<RunningClient> getClientsList() {
        LogUtils.debug("getClientsList={}", clientMap.toString());
        List<RunningClient> list = new ArrayList<>(clientMap.values());
        return list;
    }

    /**
     * 获取已连接容器数量
     */
    public int getOnlineClientNum() {
        return clientMap.size();
    }

    /**
     * Server 主入口
     * @return:
     */
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
