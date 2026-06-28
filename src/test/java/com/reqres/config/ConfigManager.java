package com.reqres.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class ConfigManager {

    private static final Properties PROPERTIES = load();

    private ConfigManager() {}

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("config.properties not found on classpath");
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load config.properties", e);
        }
        return props;
    }

    public static String get(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    public static String baseUri() {
        return get("base.uri");
    }

    public static String validApiKey() {
        return get("api.key");
    }
}
