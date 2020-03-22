package zju.vipa.aix.container.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date: 2020/1/7 19:33
 * @Author: EricMa
 * @Description: time
 */
public class TimeUtils {
        public static String getTimeStr(){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");

                String formatStr =formatter.format(new Date());
                return formatStr;
        }
}
