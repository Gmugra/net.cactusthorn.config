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
        TestConfig testConfig = ConfigFactory.create(TestConfig.class, properties);
        assertEquals("TEST", testConfig.str());
    }

    @Test public void listNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.set", "A,B,C,C");
        properties.put("test.sort", "A,B,C,C");
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, properties));
    }

    @Test public void setNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.list", "A,B,C");
        properties.put("test.sort", "A,B,C,C");
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, properties));
    }

    @Test public void sortNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.list", "A,B,C");
        properties.put("test.set", "A,B,C,C");
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, properties));
    }

    @Test public void mapEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, (Map<String, String>) null));
    }

    @Test public void classpath() {
        URI uri = URI.create("classpath:config/testconfig.properties");
        TestConfig testConfig = ConfigFactory.create(TestConfig.class, uri);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void classpathFromString() {
        TestConfig testConfig = ConfigFactory.create(TestConfig.class, "classpath:config/testconfig.properties");
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void classNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(null, "classpath:config/testconfig.properties"));
    }

    @Test public void uriNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, (URI) null));
    }

    @Test public void uriArrNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, (URI[]) null));
    }

    @Test public void uriEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, new URI[0]));
    }

    @Test public void stringNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, (String) null));
    }

    @Test public void stringArrNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, (String[]) null));
    }

    @Test public void stringEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.create(TestConfig.class, new String[0]));
    }

    @Test public void unknownLoader() {
        assertThrows(UnsupportedOperationException.class, () -> ConfigFactory.create(TestConfig.class, "mailto:this"));
    }

    @Test public void unknownBuilder() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigFactory.create(ConfigFactoryTest.class, "classpath:config/testconfig.properties"));
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
}
