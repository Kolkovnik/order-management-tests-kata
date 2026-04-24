package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final Properties props = new Properties();

    public AppConfig(String resourceName) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in != null) props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить конфиг: " + resourceName, e);
        }
    }

    public String getString(String key) {
        String value = props.getProperty(key);
        if (value == null) throw new IllegalArgumentException("Ключ не найден: " + key);
        return value;
    }
}