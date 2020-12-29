package org.zju.vipa.aix.container.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {
    private static Logger logger=LoggerFactory.getLogger(PropertyUtils.class);

    public static String getProperty(String propertyFile, String key) {
        return getProperty(propertyFile, key, null);
    }

    public static String getProperty(String propertyFile, String key, String defaultValue) {
        InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(propertyFile);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
           logger.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        return properties.getProperty(key, defaultValue);
    }

}
