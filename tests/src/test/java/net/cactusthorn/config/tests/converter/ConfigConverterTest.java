package net.cactusthorn.config.tests.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class ConfigConverterTest {

    @Test public void url() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(new URL("https://www.google.com"), config.url());
    }

    @Test public void optionalUrl() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        properties.put("ourl", "https://www.bing.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(new URL("https://www.bing.com"), config.ourl().get());
    }

    @Test public void list() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        properties.put("list", "https://www.bing.com,https://github.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(2, config.list().get().size());
    }

    @Test public void defaultUrl() throws MalformedURLException {
        ConfigConverter config = ConfigFactory.builder().build().create(ConfigConverter.class);
        assertEquals(new URL("https://github.com"), config.url());
    }
}
