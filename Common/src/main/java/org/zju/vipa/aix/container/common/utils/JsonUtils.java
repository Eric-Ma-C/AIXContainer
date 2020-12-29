package org.zju.vipa.aix.container.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;
import org.zju.vipa.aix.container.common.exception.ExceptionHandlerImpl;

import java.util.List;

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
            LoggerFactory.getLogger(JsonUtils.class).error(e.getMessage());

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

    public static String getValue(String jsonStr,String key){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Object obj = jsonObject.get(key);
        if (obj instanceof String) {
            return (String) obj;
        }
        return "";
    }

//    public static List<Object> getList(String jsonStr){
//        JSONArray jsonArray = JSON.pa(jsonStr);
//        return jsonArray;
//    }

    public static <T> List<T> getList(String jsonStr, Class<T> tClass){

        List<T> list = JSON.parseArray(jsonStr, tClass);

        return list;
    }
}
