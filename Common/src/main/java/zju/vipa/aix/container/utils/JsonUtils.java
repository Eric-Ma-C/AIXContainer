package zju.vipa.aix.container.utils;

import com.alibaba.fastjson.JSON;

/**
 * @Date: 2020/1/11 16:48
 * @Author: EricMa
 * @Description: json工具
 */
public class JsonUtils {

    public static String toJSONString(Object obj) {

        String json = "";
        try {
            /**  序列化 */
            json = JSON.toJSONString(obj);

        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }

        return json;
    }

    public static <T> T parseObject(String json, Class<T> tClass) {

        if ("".equals(json) || json == null) {
//            LogUtils.info("");
            return null;
        }

        T obj = null;

        try {
            /** 反序列化 */
            obj = JSON.parseObject(json, tClass);
        } catch (Exception e) {
            LogUtils.debug(TokenUtils.getDeviceToken(),"Json parse failed:" + json);
            ExceptionUtils.handle(e);
        }

        return obj;
    }
}
