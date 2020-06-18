package org.zju.vipa.aix.container.center.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {

    public static String getProperty(String propertyFile, String key) {
        return getProperty(propertyFile, key, null);
    }

    public static String getProperty(String propertyFile, String key, String defaultValue) {
        InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(propertyFile);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            ExceptionUtils.handle(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    ExceptionUtils.handle(e);
                }
            }
        }

        return properties.getProperty(key, defaultValue);
    }

}
