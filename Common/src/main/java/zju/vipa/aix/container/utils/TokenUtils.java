package zju.vipa.aix.container.utils;

import zju.vipa.aix.container.config.Config;
import zju.vipa.aix.container.config.NetworkConfig;
import zju.vipa.aix.container.utils.FileUtils;

/**
 * @Date: 2020/3/27 9:17
 * @Author: EricMa
 * @Description: 管理平台容器token
 */
public class TokenUtils {
    /**
     *  通过配置文件判断调用本方法的是平台还是容器client
     * 如果是容器，则获取当前容器token
     */
    public static String  getDeviceToken() {
        String token = FileUtils.getContent(Config.TOKEN_FILE);
        if (token==null) {
             token = NetworkConfig.CENTER_TOKEN;
//            token = NetworkConfig.TEST_CONTAINER_TOKEN;
        }
//        LogUtils.debug("getDeviceToken="+token);
        return token;
    }

    /**
     * 获取token后缀
     */
    public static String getTokenSuffix() {
        String token = getDeviceToken();
        return token.substring(token.length() - 9);
    }
}
