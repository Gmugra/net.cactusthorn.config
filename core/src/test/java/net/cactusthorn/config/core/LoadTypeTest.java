package net.cactusthorn.config.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.LoadStrategy;

public class LoadTypeTest {

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void merge() {
        TestConfig testConfig = ConfigFactory.create(TestConfig.class, "classpath:config/testconfig.properties",
                "classpath:config/testconfig2.properties");
        assertEquals("SSSSSSSS", testConfig.dstr());
    }

    @Test public void first() {
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").build()
                .create(TestConfig.class);
        assertEquals("A", testConfig.dstr());
    }

    @Test public void withMapMerge() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.dstr", "FROMMAP");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.MERGE)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").setSource(properties)
                .build().create(TestConfig.class);
        assertEquals("FROMMAP", testConfig.dstr());
    }

    @Test public void withMapFirst() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.dstr", "FROMMAP");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").setSource(properties)
                .build().create(TestConfig.class);
        assertEquals("FROMMAP", testConfig.dstr());
    }

    @Test public void firstSkipEmpty() {
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/notExists.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("A", testConfig.dstr());
    }

    @Test public void firstNotExists() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/notExists.properties").build().create(TestConfig.class));
    }
}
