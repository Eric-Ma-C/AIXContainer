package org.zju.vipa.aix.container.center;

import org.zju.vipa.aix.container.center.netty.NettyTcpServer;
import org.zju.vipa.aix.container.center.util.JwtUtils;
import org.zju.vipa.aix.container.center.dubbo.RpcServer;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;

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

            id = JwtUtils.decodeClinetIdByToken(token);
            if (id != null) {
                cachedTokenMap.put(token, id);
            }
        }


        return id;
    }

    public static void main(String[] args) {
        System.out.println("Starting AIX Container Manager...");




        /** 处理主线程的未捕获异常 */
        ExceptionUtils.setDefaultUncaughtExceptionHandler();
        /** dubbo */
        RpcServer.getInstance().start();






        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Starting Netty Server...");
                /**  启动Netty tcp服务器 */
                NettyTcpServer.start();
            }
        }).start();



//        System.out.println("readInit1");
//        LogReader.readInit();
//        System.out.println("readInit2");
//        String s = LogReader.readLine();
//        s+=LogReader.readLine();
//        s+=LogReader.readLine();
//        s+=LogReader.readLine();
//        s+=LogReader.readLine();
//        System.out.println(s);
//        LogReader.stop();

//
//        try {
//            CenterTcpServer.getInstance().start();
//        } catch (IOException e) {
//            ExceptionUtils.handle(e);
//        }


    }
}
