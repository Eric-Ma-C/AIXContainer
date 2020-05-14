package org.zju.vipa.aix.container.utils;

import com.alibaba.fastjson.JSON;

/**
 * @Date: 2020/1/11 16:48
 * @Author: EricMa
 * @Description: json工具
 */
public class JsonUtils {

    public static String toJSONString(Object obj) {

        String json = "";
        /**  序列化 */
        json = JSON.toJSONString(obj);
        return json;
    }

    public static <T> T parseObject(String json, Class<T> tClass) {

        if ("".equals(json) || json == null) {
            return null;
        }

        T obj = null;

        /** 反序列化 */
        obj = JSON.parseObject(json, tClass);


        return obj;
    }
}
