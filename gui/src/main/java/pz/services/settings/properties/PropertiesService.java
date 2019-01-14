package pz.services.settings.properties;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertiesService {
    private static PropertiesService instance;
    private static Properties properties;

    private PropertiesService() {
        properties = new Properties();
        try {
            properties.load(Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("application.properties"));
        } catch (IOException e) {
            log.error("Unable to load properties.", e);
        }
    }

    public static synchronized PropertiesService getInstance() {
        if (instance == null) {
            instance = new PropertiesService();
        }
        return instance;
    }

    public String get(Property property) {
        return properties.getProperty(property.getValue());
    }
}
