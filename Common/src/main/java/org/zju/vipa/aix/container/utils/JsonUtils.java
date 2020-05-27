package org.zju.vipa.aix.container.utils;

import com.alibaba.fastjson.JSON;
import org.zju.vipa.aix.container.exception.ExceptionHandlerImpl;

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
        }catch (Exception e){
          //todo 0523
       new ExceptionHandlerImpl().handle(e);
        }

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
