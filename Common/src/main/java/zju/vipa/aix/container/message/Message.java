package zju.vipa.aix.container.message;


import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2020/1/9 21:59
 * @Author: EricMa
 * @Description: 容器与平台通信的消息体
 */
public class Message {
    /**
     * 结束字符串
     */
    public static final String END_STRING = "EOF-AIX-MESSAGE";
    /**
     * 消息字符编码格式
     */
    public static final String CHARSET_NAME = "UTF-8";

    private Intent intent;
    private String value;
    /**
     * 每个容器的唯一身份标识
     */
    private String token;
    /**
     * 自定义数据 键值对
     */
    private Map<String, String> customDataMap =null;


    /**
     * fastjson需要
     */
    public Message() {
    }

    protected Message(Intent intent, String value, String token) {
        this.intent = intent;
        this.value = value;
        this.token = token;


    }

    protected Message(Intent intent, String token) {
        this.intent = intent;
        this.value = "";
        this.token = token;
    }


    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected Map<String, String> getCustomDataMap() {
        return customDataMap;
    }

    protected void setCustomDataMap(Map<String, String> customDataMap) {
        this.customDataMap = customDataMap;
    }

    public String getCustomData(String key) {
        if(customDataMap ==null){
            return null;
        }
        return customDataMap.get(key);
    }

    public void addCustomData(String k, String v) {
        if (customDataMap ==null){
            customDataMap =new HashMap<>();
        }
        customDataMap.put(k,v);
    }

    @Override
    public String toString() {
        return "Message{" +
            "intent=" + intent +
            ", value='" + value + '\'' +
            ", token='" + token + '\'' +
            ", customDataMap=" + customDataMap +
            '}';
    }

    //    @Override
//    public String toString() {
//        return "Message{" +
//            "intent=" + intent +
//            ", value='" + value + '\'' +
//            ", token='" + token + '\'' +
//            '}';
//    }
}
