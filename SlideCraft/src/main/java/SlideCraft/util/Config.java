package SlideCraft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Config INSTANCE;
    private Properties properties = new Properties();

    private Config() {
        try {
            File filePath = new File(getConfigFolderPath(), "slidecraft.properties");
            if (filePath.exists() && filePath.isFile()) {
                InputStream is = new FileInputStream(filePath);
                properties.load(is);
                is.close();
            }
        } catch (Exception e) {
            System.out.println("An error occurred during loading configuration."+e);
        }
    }

    private File getConfigFolderPath() {
        File configurationFolderPath = null;
        try {
            configurationFolderPath = new File(Config.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).getParentFile();
        } catch (Exception e) {
            System.out.println("Could not determine configuration folder path."+e);
        }
        return configurationFolderPath;
    }

    private <T> T getValue(String key, T defaultValue, Class<T> type) {
        T value = defaultValue;
        String prop = properties.getProperty(key);
        if (prop != null) {
            try {
                value = type.getConstructor(String.class).newInstance(prop);
            } catch (Exception e) {
                System.out.println(String.format("Could not parse value as [%s] for key [%s]", type.getName(), key));
            }
        }
        return value;
    }

    public Boolean is(String key, Boolean defaultValue) {
        return getValue(key, defaultValue, Boolean.class);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return getValue(key, defaultValue, Integer.class);
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String[] getArray(String key, String[] defaultValue) {
        String prop = properties.getProperty(key);
        if (prop != null) {
            return prop.split(",");
        }
        return defaultValue;
    }

    public static synchronized Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

}
