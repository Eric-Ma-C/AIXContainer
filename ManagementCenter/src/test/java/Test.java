import zju.vipa.aix.container.center.ManagementCenter;
import zju.vipa.aix.container.config.NetworkConfig;

/**
 * @Date: 2020/3/29 22:53
 * @Author: EricMa
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        ManagementCenter.getInstance().getIdByToken(NetworkConfig.TEST_CONTAINER_TOKEN);
    }
}
