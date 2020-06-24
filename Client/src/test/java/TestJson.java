import org.zju.vipa.aix.container.common.message.Intent;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.utils.JsonUtils;

/**
 * @Date: 2020/4/17 23:38
 * @Author: EricMa
 * @Description:
 */
public class TestJson {
    public static void main(String[] args) {
        Message msg=new Message(Intent.SHELL_ERROR_HELP,"cmd1","token");
        msg.setIntent(Intent.SHELL_ERROR_HELP);
        msg.setToken("123");
        msg.setValue("cmd");
        msg.addCustomData("k","v");


        String json= JsonUtils.toJSONString(msg);
        Message m=  JsonUtils.parseObject(json,Message.class);
        System.out.println(m.toString());
    }
}
