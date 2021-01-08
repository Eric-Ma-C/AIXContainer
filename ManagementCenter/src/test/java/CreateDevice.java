import org.zju.vipa.aix.container.center.util.JwtUtils;
import org.zju.vipa.aix.container.common.db.entity.atlas.AixDevice;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Date: 2020/12/30 20:11
 * @Author: EricMa
 * @Description:
 */
public class CreateDevice {
    public static void main(String[] args) {
//        generateShell();
    }

    private static void generateShell() {

        final int COUNT = 10;
        String[] nameList = new String[COUNT];
        for (int i = 0; i < COUNT; i++) {
            nameList[i] = "c" + i;
        }
        StringBuilder shellStr=new StringBuilder("#!/bin/bash\n");
        for (String name : nameList) {
            String token = JwtUtils.createJWT(name, -1);
            AixDevice aixDevice =new AixDevice();
            aixDevice.setDevice_name(name);
            aixDevice.setInfo("aix test device");
            aixDevice.setToken(token);
            aixDevice.setUser_id(1);
            /** 插入数据库 */
//            DbManager.getInstance().insertClient(aixDevice);

            /** eg. stax c1 eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxIn0.RwXicwD8L7Whd5PUgIDsDOY_4XFMF5D4D9WLlqizPEg /nfs2/aix/models/training '"device=0"' */
            shellStr.append("stax ");
            shellStr.append(name);
            shellStr.append(" ");
            shellStr.append(token);
            shellStr.append(" /nfs2/aix/models/training '\"device=0\"' &&\n");
        }

        try {
            String str = shellStr.toString();
            str=str.substring(0,str.length()-3);
            BufferedWriter out = new BufferedWriter(new FileWriter("staxxxx.sh"));
            out.write(str);
            out.close();
            System.out.println("文件创建成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
