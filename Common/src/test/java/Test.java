import org.zju.vipa.aix.container.common.env.AptSource;
import org.zju.vipa.aix.container.common.message.Message;
import org.zju.vipa.aix.container.common.utils.ByteUtils;
import org.zju.vipa.aix.container.common.utils.TimeUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @Date: 2020/3/29 20:10
 * @Author: EricMa
 * @Description: 测试
 */
public class Test {
    public static void main(String[] args) throws IOException {
//        TestSin sin= (TestSin) TestSin.getInstance();
//        sin.haha();
        byte[] bytes = ByteUtils.longToByte8(647L);
//        byte[] bytes = ByteUtils.intToByte4(647);
//        String s = new String(bytes, "UTF-8");
        String s = new String(bytes,"US-ASCII");
        byte[] by=s.getBytes("US-ASCII");
        System.out.println(by);




//        Singleton singleton=Singleton.getInstance();
//        changeAptTest();
//        System.out.println(PropertyUtils.getProperty("common.properties","server.ip","127.0.0.1"));
    }

    private static void TimeUtilsTest(){
        System.out.println(TimeUtils.getInterval(234));
        System.out.println(TimeUtils.getInterval(1010));
        System.out.println(TimeUtils.getInterval(345000));
        System.out.println(TimeUtils.getInterval(3600001));
        System.out.println(TimeUtils.getInterval(86400200));
        System.out.println(TimeUtils.getInterval(4325234546435645L ));
    }


    private static void changeAptTest() {
        for (int i = 0; i < 5; i++) {
            System.out.println(AptSource.nextUrl()+'\n');
        }
    }





    /**
     * 按token存储的消息队列,存放需要顺序执行的消息，<token,messages>
     */
    private Map<String, ConcurrentLinkedDeque<Message>> serialTaskMessageMap;

    /**
     * 按token存储的消息队列,存放随心跳信息发送的消息,一般为轻量级控制操作,
     * 不应该影响到正在执行的任务,如开启详细实时日志上传，<token,messages>
     */
    private Map<String, ConcurrentLinkedDeque<Message>> heartbeatMessageMap;













}



