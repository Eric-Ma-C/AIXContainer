import org.zju.vipa.aix.container.center.util.JwtUtils;
import org.zju.vipa.aix.container.config.NetworkConfig;

/**
 * @Date: 2020/3/29 22:53
 * @Author: EricMa
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
//        ManagementCenter.getInstance().getIdByToken(NetworkConfig.TEST_CONTAINER_TOKEN);
        generateToken();
    }

    private static void generateToken(){
        String[] idList={
            "111111111111111111111",
            "222222222222222222222",
            "333333333333333333333",
            "4444444444444444444444",
            "55555555555555555555",
            "bfeb717242c94d60b73ad6dd695164e3",
            NetworkConfig.CENTER_ID,
            "null"
        };
        for (String s : idList) {
            String token=JwtUtils.createJWT(s, -1);
            System.out.println(token);
//            System.out.println("  "+JwtUtil.verify(token));
            System.out.println("  "+ JwtUtils.decodeClinetIdByToken(token));

        }
    }
}
