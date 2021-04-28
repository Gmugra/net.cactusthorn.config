package net.cactusthorn.config.core;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.LoadStrategy;

public class ConfigFactoryTest {

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void map() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.list", "A,B,C");
        properties.put("test.set", "A,B,C,C");
        properties.put("test.sort", "A,B,C,C");
        properties.put("test.url", "https://google.com");
        TestConfig testConfig = ConfigFactory.builder().setSource(properties).build().create(TestConfig.class);
        assertEquals("TEST", testConfig.str());
    }

    @Test public void listNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.set", "A,B,C,C");
        properties.put("test.sort", "A,B,C,C");
        ConfigFactory factory = ConfigFactory.builder().setSource(properties).build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(TestConfig.class));
    }

    @Test public void setNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.list", "A,B,C");
        properties.put("test.sort", "A,B,C,C");
        ConfigFactory factory = ConfigFactory.builder().setSource(properties).build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(TestConfig.class));
    }

    @Test public void sortNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.list", "A,B,C");
        properties.put("test.set", "A,B,C,C");
        ConfigFactory factory = ConfigFactory.builder().setSource(properties).build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(TestConfig.class));
    }

    @Test public void classpath() {
        URI uri = URI.create("classpath:config/testconfig.properties");
        TestConfig testConfig = ConfigFactory.builder().addSource(uri).build().create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void classpathFromString() {
        TestConfig testConfig = ConfigFactory.builder().addSource("classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void classpathAndMap() {
        URI uri = URI.create("classpath:config/testconfig.properties");
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        TestConfig testConfig = ConfigFactory.builder().addSource(uri).setSource(properties).build().create(TestConfig.class);
        assertEquals("TEST", testConfig.str());
    }

    @Test public void systemProperty() {
        System.setProperty("testit", "config");
        TestConfig testConfig = ConfigFactory.builder().addSource("classpath:{testit}/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void merge() {
        TestConfig testConfig = ConfigFactory.builder().addSource("classpath:config/testconfig.properties", "classpath:test.properties")
                .build().create(TestConfig.class);
        assertEquals("bbb", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void merge2() {
        System.setProperty("aaa", "PROP");
        TestConfig testConfig = ConfigFactory.builder()
                .addSource("classpath:config/testconfig.properties", "system:properties", "classpath:test.properties").build()
                .create(TestConfig.class);
        assertEquals("PROP", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void first() {
        System.setProperty("aaa", "PROP");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "system:properties", "classpath:test.properties").build()
                .create(TestConfig.class);
        assertEquals("ddd", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void setSourceNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setSource(null));
    }

    @Test public void addSourceUriNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource((URI[]) null));
    }

    @Test public void addSourceStrNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource((String[]) null));
    }

    @Test public void addSourceUriEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource(new URI[0]));
    }

    @Test public void addSourceStrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource(new String[0]));
    }

    @Test public void addSourceStrNullElement() {
        ConfigFactory.builder().addSource(new String[] { null });
    }

    @Test public void loaderNotFound() {
        ConfigFactory factory = ConfigFactory.builder().addSource("system:yyy").build();
        assertThrows(UnsupportedOperationException.class, () -> factory.create(TestConfig.class));
    }

    @Test public void unknownBuilder() {
        ConfigFactory factory = ConfigFactory.builder().build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(String.class));
    }
}
