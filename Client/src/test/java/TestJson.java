import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/4/17 23:38
 * @Author: EricMa
 * @Description:
 */
public class TestJson {
    public static void main(String[] args) {
//        String s="[{\"cmd\":\"111\"},{\"cmd\":\"222\"},{\"cmd\":\"333\"}]";
        String s="[\"111\",\"222\"]";
        List<String> list = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(s);
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.getString(i));
        }
        System.out.println(list);
    }

    public static void t() {
        Message msg = new Message(Intent.SHELL_ERROR_HELP, "cmd1", "token");
        msg.setIntent(Intent.SHELL_ERROR_HELP);
        msg.setToken("123");
        msg.setValue("cmd");
        msg.addCustomData("k", "v");


        String json = JsonUtils.toJSONString(msg);
        Message m = JsonUtils.parseObject(json, Message.class);
        System.out.println(m.toString());
    }
}
