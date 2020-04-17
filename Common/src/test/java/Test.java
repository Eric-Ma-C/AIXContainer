import zju.vipa.aix.container.config.NetworkConfig;
import zju.vipa.aix.container.utils.TimeUtils;

import java.io.IOException;
import java.util.logging.SocketHandler;

/**
 * @Date: 2020/3/29 20:10
 * @Author: EricMa
 * @Description: 测试
 */
public class Test {
    public static void main(String[] args) throws IOException {

    }

    private static void TimeUtilsTest(){
        System.out.println(TimeUtils.getInterval(234));
        System.out.println(TimeUtils.getInterval(1010));
        System.out.println(TimeUtils.getInterval(345000));
        System.out.println(TimeUtils.getInterval(3600001));
        System.out.println(TimeUtils.getInterval(86400200));
        System.out.println(TimeUtils.getInterval(4325234546435645L ));
    }

}
