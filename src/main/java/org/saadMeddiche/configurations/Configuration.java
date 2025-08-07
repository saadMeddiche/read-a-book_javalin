package org.saadMeddiche.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Configuration {

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    protected Properties prop = new Properties();

    {
        loadProperties();
    }

    public final String getProperty(String key) {
        return prop.getProperty(key);
    }

    public final String getProperty(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    protected void loadProperties() {
        try (InputStream inputStream = this.getClass().getResourceAsStream(path())) {
            prop.load(inputStream);
        } catch (IOException e) {
            log.error("Error loading properties file", e);
        }
    }

    protected abstract String path();

}
