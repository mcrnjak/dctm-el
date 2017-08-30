package mc.dctm.el.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for accessing configuration properties.
 *
 * @author Milan Crnjak
 */
public class DctmElProperties {

    private static class HOLDER {
        private static DctmElProperties instance = new DctmElProperties();
    }

    private Properties properties;

    private DctmElProperties() {
        properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("dctm-el.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DctmElProperties getInstance() {
        return HOLDER.instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
