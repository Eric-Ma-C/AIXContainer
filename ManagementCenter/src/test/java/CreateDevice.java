import org.zju.vipa.aix.container.center.db.AtlasDbManager;
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

//        correctToken();
        correctName();
    }

    private static void generateShell() {

        final int COUNT = 10;
        String[] nameList = new String[COUNT+1];
        String[] tokenList = new String[COUNT+1];
        StringBuilder shellStr=new StringBuilder("#!/bin/bash\n");
        for (int i = 1; i <= COUNT; i++) {
            nameList[i] = "c" + i;
            tokenList[i]=JwtUtils.createJWT(i+"", -1);

            AixDevice aixDevice =new AixDevice();
            aixDevice.setDevice_name(nameList[i]);
            aixDevice.setInfo("aix test device");
            aixDevice.setToken(tokenList[i]);
            aixDevice.setUser_id(1);
            /** 插入数据库 */
//            DbManager.getInstance().insertClient(aixDevice);

            /** eg. stax c1 eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxIn0.RwXicwD8L7Whd5PUgIDsDOY_4XFMF5D4D9WLlqizPEg /nfs2/aix/models/training '"device=0"' */
            shellStr.append("stax ");
            shellStr.append(nameList[i]);
            shellStr.append(" ");
            shellStr.append(tokenList[i]);
            shellStr.append(" /nfs2/aix/models/training &&\n");
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


    private static void correctToken(){
        int MAX_ID=105;
        for (int id = 1; id <= MAX_ID; id++) {
            String token = JwtUtils.createJWT(id+"", -1);
            AtlasDbManager.getInstance().updateDeviceTokenById(id+"",token);
        }
    }

    private static void correctName(){
        int MAX_ID=105;
        for (int id = 1; id <= MAX_ID; id++) {
            String name = "c"+id;
            AtlasDbManager.getInstance().updateDeviceNameById(id+"",name);
        }
    }
}
