package util;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyLoader {
    private static final String pathToPropertyFile = "src/test/resources/config.properties";

    private static Properties getPropertyFile(String pathToPropertyFile){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(pathToPropertyFile));
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
        return properties;
    }
    public static String getProperty(String key){
        return getPropertyFile(pathToPropertyFile).getProperty(key);
    }
}
