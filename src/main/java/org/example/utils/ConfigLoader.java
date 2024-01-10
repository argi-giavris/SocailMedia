package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {

    public static AppConfig loadConfig(String filename)  {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                throw new IOException("Configuration file not found: " + filename);
            }
            return mapper.readValue(is, AppConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
