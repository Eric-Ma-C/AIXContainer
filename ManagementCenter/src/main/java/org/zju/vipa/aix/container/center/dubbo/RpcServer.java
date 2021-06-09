package org.zju.vipa.aix.container.center.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.zju.vipa.aix.container.api.AIXClientCenterService;
import org.zju.vipa.aix.container.common.config.NetworkConfig;
import org.zju.vipa.aix.container.common.exception.AIXBaseException;
import org.zju.vipa.aix.container.common.exception.ExceptionCodeEnum;
import org.zju.vipa.aix.container.common.utils.PropertyUtils;


/**
 * @Date: 2020/5/31 10:04
 * @Author: EricMa
 * @Description: dubbo服务提供者配置
 */
public class RpcServer {
    /** 服务实现 */
    AIXClientCenterService service;
    /** 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口 */
    ServiceConfig<AIXClientCenterService> serviceConfig;

    private static class RpcServerHolder {

        private static final RpcServer INSTANCE = new RpcServer();

    }
    private RpcServer() {
        if (RpcServerHolder.INSTANCE != null) {
            throw new AIXBaseException(ExceptionCodeEnum.SINGLETON_MULTI_INSTANCE);
        }
    }

    public static RpcServer getInstance() {
        return RpcServerHolder.INSTANCE;
    }


    public void start() {

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName(PropertyUtils.getProperty("common.properties","dubbo.provider.application.name","default-dubbo-provider"));

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
//        registry.setAddress("zookeeper://127.0.0.1:2181");
        registry.setAddress(NetworkConfig.ZK_SERVER_URL);



//    registry.setUsername("aaa");
//    registry.setPassword("bbb");

        // 服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(NetworkConfig.DUBBO_SERVER_PORT);
        protocol.setThreads(5);


        service = new AIXClientCenterServiceImpl();

        // 服务提供者暴露服务配置
        // todo 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(application);
        // 多个注册中心可以用setRegistries()
        serviceConfig.setRegistry(registry);
        // 多个协议可以用setProtocols()
        serviceConfig.setProtocol(protocol);
        serviceConfig.setInterface(AIXClientCenterService.class);
        serviceConfig.setRef(service);
        serviceConfig.setVersion("1.0.0");
        serviceConfig.setTimeout(10000);

        /** 用于区分不同的dubbo服务 */
        serviceConfig.setGroup(PropertyUtils.getProperty("common.properties","dubbo.provider.group","default-group"));

        // 暴露及注册服务
        serviceConfig.export();
    }

}
