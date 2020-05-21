package org.zju.vipa.aix.container.client.utils;

import org.zju.vipa.aix.container.config.Config;
import org.zju.vipa.aix.container.config.NetworkConfig;
import org.zju.vipa.aix.container.config.DebugConfig;

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

        if (DebugConfig.IS_LOCAL_DEBUG){
            return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTExMTExMTExMTExMTExMTExMTEifQ.it6iBaBmEkYIkJ49_2dCUL6nSqH7SHTJJ2IpoM8-nAs";
        }


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
    public static String getSuffix() {
        String token = getDeviceToken();
        return token.substring(token.length() - 9);
    }

    /**
     * 获取token后缀
     */
    public static String getSuffix(String token) {
        return token.substring(token.length() - 9);
    }
}
