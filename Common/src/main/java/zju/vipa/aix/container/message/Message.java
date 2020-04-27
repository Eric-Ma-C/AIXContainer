package zju.vipa.aix.container.message;


import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2020/1/9 21:59
 * @Author: EricMa
 * @Description: 容器与平台通信的消息体
 * todo 确认 fastjson 反序列化时，若没有public的getter和setter，则为该变量默认初始值，如0，null
 *                      无参构造函数和全参构造函数至少有一个，不限制访问修饰符
 *                      不需要重载tostring
 *
 */
public class Message {
    /**
     * 结束字符串
     * 不用了，改为前两字节表示长度
     */
//    public static final String END_STRING = "EOF-AIX-MESSAGE";


    /**
     * 消息字符编码格式
     */
    public static final String CHARSET_NAME = "UTF-8";

    protected Intent intent;
    protected String value;
    /**
     * 每个容器的唯一身份标识
     */
    private String token;
    /**
     * 自定义数据 键值对
     */
    protected Map<String, String> customDataMap =null;

//    private String s;

    /**
     * fastjson需要
     */
    private Message() {
    }


    public Message(Intent intent, String value, String token) {
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
    public String getTokenSuffix() {
        return token.substring(token.length()-9);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, String> getCustomDataMap() {
        return customDataMap;
    }

    public void setCustomDataMap(Map<String, String> customDataMap) {
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
            customDataMap =new HashMap<>(2);
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


}
