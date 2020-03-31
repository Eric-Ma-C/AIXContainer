package zju.vipa.aix.container.center.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author sontal
 * @version 1.0
 * @date 2020/2/27 10:14
 */
public class PropertyUtils {

    private static Properties properties;

    /**
     * static init block, read the properties file in the first use
     */
    static{
        InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream("custom.properties");
        properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    static String getProperty(String key, String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

}
