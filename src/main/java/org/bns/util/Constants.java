package org.bns.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants{
    public static final String USER_QQ = getConfigValue("userQQ", "454299035");
    public static final String ADMIN_QQ = getConfigValue("adminQQ", "454299035");
    public static final String ADMIN_VX = getConfigValue("adminVX", "454299035");
    public static final int LIMIT_YEAR = getConfigValue("limitYear", 2023);
    public static final int LIMIT_MONTH= getConfigValue("limitMonth", 10);
    public static final int LIMIT_DAY = getConfigValue("limitDay", 31);


    private static String getConfigValue(String key, String defaultValue) {
        Properties properties = new Properties();
        try (InputStream input = Constants.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
            return properties.getProperty(key, defaultValue);
        } catch (IOException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    private static int getConfigValue(String key, int defaultValue) {
        String value = getConfigValue(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}