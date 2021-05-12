package org.zju.vipa.aix.container.center;

import org.zju.vipa.aix.container.api.entity.RunningClient;
import org.zju.vipa.aix.container.api.entity.TaskBriefInfo;
import org.zju.vipa.aix.container.center.db.AixDbManager;
import org.zju.vipa.aix.container.center.db.AtlasDbManager;
import org.zju.vipa.aix.container.center.dubbo.RpcServer;
import org.zju.vipa.aix.container.center.log.LogUtils;
import org.zju.vipa.aix.container.center.netty.NettyTcpServer;
import org.zju.vipa.aix.container.center.network.SocketServer;
import org.zju.vipa.aix.container.center.task.TaskManager;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.util.GpuUtils;
import org.zju.vipa.aix.container.common.config.ClientConfig;
import org.zju.vipa.aix.container.common.config.NetworkConfig;
import org.zju.vipa.aix.container.common.db.entity.aix.KnownError;
import org.zju.vipa.aix.container.common.db.entity.aix.Source;
import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;
import org.zju.vipa.aix.container.common.env.AptSource;
import org.zju.vipa.aix.container.common.env.ErrorParser;
import org.zju.vipa.aix.container.common.env.KnownErrorRuntime;
import org.zju.vipa.aix.container.common.env.PipSource;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.message.GpuInfo;
import org.zju.vipa.aix.container.common.utils.JsonUtils;
import org.zju.vipa.aix.container.common.utils.TimeUtils;

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

    /**
     * 获取容器正在运行的命令
     */
    public String getRunningCmdsByToken(String token) {
        RunningClient client = clientMap.get(token);
        if (client != null) {
            return client.getRunningCmds();
        } else {
            return null;
        }
    }

    /**
     * 获取容器正在运行的任务日志
     *
     * @return
     */
    public List<String> getTaskLogsByToken(String token) {
        RunningClient client = clientMap.get(token);
        if (client != null) {
            return client.getTaskLogList();
        } else {
            return null;
        }
    }

    /**
     * 注册容器
     * 新容器接入平台
     *
     * @param token
     * @return: id null代表注册失败
     */
    public String registerClient(String token, String hostIp) {
        String id = getClientIdByToken(token);
        AixDevice device;
        if (id == null) {
            /** 去数据库检查token   */
            device = AtlasDbManager.getInstance().getClientByToken(token);

//            id = JwtUtils.decodeClinetIdByToken(token);
            if (device != null) {
                id = String.valueOf(device.getId());
                /** 新设备接入 */
//                clientMap.put(token, new RunningClient(id, token, TimeUtils.getCurrentTimeStr()));
                clientMap.put(token, new RunningClient(device, hostIp));

            }
        } else {
            LogUtils.worning("容器重复注册,token={}", token);
        }
        return id;
    }

    /**
     * 根据token获取容器id
     *
     * @param token
     * @return: java.lang.String 返回null代表无此设备
     */
    public String getClientIdByToken(String token) {
        RunningClient client = clientMap.get(token);
        String id = null;
        if (client != null) {
            id = client.getId();
        }
//        LogUtils.debug("getIdByToken({})={}",token,id);
        return id;
    }

    public void updateGpuInfo(String token, GpuInfo info) {
        RunningClient client = clientMap.get(token);
        if (client == null) {
            LogUtils.worning("updateGpuInfo()未找到在线容器token:{}", token);
            return;
        }
        for (GpuInfo.Gpu gpu : info.getGpus()) {
            /** 更新算力 */
            gpu.setCalPower(GpuUtils.getGpuPower(gpu.getName()));
        }
        client.setGpuInfo(info);
        /** 更新心跳时间 */
        client.setLastHeartbeat(System.currentTimeMillis());
        AtlasDbManager.getInstance().updateDeviceGpuDetailById(client.getId(), JsonUtils.toJSONString(info));
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

//        LogUtils.debug("updateLatestError={}", client.getLatestErrors());
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
            RunningClient client = entry.getValue();
            long lastTime = client.getLastHeartbeat();
            if (System.currentTimeMillis() - lastTime > ClientConfig.HEARTBEATS_INTERVAL_MS * 3) {

                LogUtils.debug("******* Remove Dead Client: current={} LastHeartbeat()={} *******", TimeUtils.getCurrentTimeStr(), TimeUtils.getTimeStr(entry.getValue().getLastHeartbeat()));

                /** 超过心跳间隔时间3倍未收到,认为client已离线 */
                LogUtils.info("******* Remove Dead Client: token={} id={} *******", client.getToken(), client.getId());

                /** 删除TaskManger中的容器信息  */
                TaskManager.getInstance().removeDeadClientByToken(client.getToken());
                /** 从在线容器列表中删除记录 */
                iterator.remove();
            }
        }
    }

    /**
     * 获取已连接容器列表
     */
    public List<RunningClient> getClientsList() {
//        LogUtils.debug("getClientsList={}", clientMap.toString());
        StringBuilder sb = new StringBuilder("getClientsList=");
        for (RunningClient value : clientMap.values()) {
            sb.append(value.getId());
            sb.append(",");
        }
        LogUtils.debug(sb.toString());

        List<RunningClient> list = new ArrayList<>(clientMap.values());
        return list;
    }

    public static void refreshPipSource() {
        List<Source> pipSourceList = AixDbManager.getInstance().getPipSourceList();

        if (pipSourceList != null) {
            for (Source source : pipSourceList) {
                LogUtils.info("refreshPipSource={}", source);
            }
            PipSource.refreshSource(pipSourceList);
        } else {
            LogUtils.error("aptSourceList=null!");
        }

    }
    public static void refreshKnownError() {
        List<KnownError> knownErrorList = AixDbManager.getInstance().getKnownErrorList();
        if (knownErrorList == null) {
            LogUtils.error("knownErrorList is null!");
        } else {
            LogUtils.debug("knownErrorList.size={}", knownErrorList.size());
            List<KnownErrorRuntime> knownErrorRuntimeList = new ArrayList<>();
            for (KnownError knownError : knownErrorList) {
                knownErrorRuntimeList.add(new KnownErrorRuntime(knownError));
                LogUtils.debug(knownError.toString());
            }
            ErrorParser.refreshRuntimeErrorList(knownErrorRuntimeList);
        }
    }

    public static void refreshAptSource() {
        List<Source> aptSourceList = AixDbManager.getInstance().getAptSourceList();

        if (aptSourceList != null) {
            LogUtils.info("refreshAptSource={}", aptSourceList.toArray());
            for (Source source : aptSourceList) {
                LogUtils.info("refreshAptSource={}", source);
            }
            AptSource.refreshSource(aptSourceList);
        } else {
            LogUtils.error("aptSourceList=null!");
        }

    }

    /**
     * 获取已连接容器数量
     */
    public int getOnlineClientNum() {
        return clientMap.size();
    }

    private static void initFromDB() {
        /** 首次从数据库加载自动环境修复 */
        refreshKnownError();
        refreshAptSource();
        refreshPipSource();
    }

    /**
     * Server 主入口
     *
     * @return:
     */
    public static void main(String[] args) {
        LogUtils.info("Starting AIX Container Manager...");


        /** 处理主线程的未捕获异常 */
        ExceptionUtils.setDefaultUncaughtExceptionHandler();


        /** 初始化 */
        initFromDB();


        /** dubbo */
        RpcServer.getInstance().start();


        /** 启动tcp服务 */
        if (NetworkConfig.SERVER_USE_NETTY) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.info("Starting Netty Server...");
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
