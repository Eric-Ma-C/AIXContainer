import zju.vipa.aix.container.config.NetworkConfig;
import zju.vipa.aix.container.utils.FileUtils;
import zju.vipa.aix.container.utils.JwtUtil;

import javax.xml.bind.SchemaOutputResolver;

/**
 * @Date: 2020/3/29 20:10
 * @Author: EricMa
 * @Description: 测试
 */
public class Test {
    public static void main(String[] args) {
        generateToken();

    }

    private static void generateToken(){
        String[] idList={
            "111111111111111111111",
            "222222222222222222222",
            "333333333333333333333",
            "4444444444444444444444",
            "55555555555555555555",
            NetworkConfig.CENTER_ID,
            "null"
        };
        for (String s : idList) {
            String token=JwtUtil.createJWT(s, -1);
            System.out.println(token);
//            System.out.println("  "+JwtUtil.verify(token));
            System.out.println("  "+JwtUtil.decodeClinetIdByToken(token));

        }
    }
}
