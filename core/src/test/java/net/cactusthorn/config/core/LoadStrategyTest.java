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

public class LoadStrategyTest {

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void merge() {
        TestConfig testConfig = ConfigFactory.builder()
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").build()
                .create(TestConfig.class);
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

    @Test public void setLoadStrategyNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setLoadStrategy(null));
    }

    @Test public void firstIgnoreCaseHolder() {
        Map<String, String> manual = new HashMap<>();
        manual.put("test.String", "STR");
        ConfigHolder holder = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST_KEYCASEINSENSITIVE).setSource(manual).build()
                .configHolder();
        assertEquals("STR", holder.getString("test.strinG"));
    }

    @Test public void firstIgnoreCase() {
        Map<String, String> manual = new HashMap<>();
        manual.put("test.String", "STR");
        manual.put("TEST.LIST", "qq,ss");
        manual.put("tEst.SET", "a,v,x");
        manual.put("tEst.sort", "a,v,v");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST_KEYCASEINSENSITIVE).setSource(manual).build()
                .create(TestConfig.class);
        assertEquals("STR", testConfig.str());
    }

    @Test public void mergeIgnoreCase() {
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.MERGE_KEYCASEINSENSITIVE)
                .addSource("classpath:config/testconfigIgnoreKeyCase.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("IGNORECASE", testConfig.str());
    }
}
