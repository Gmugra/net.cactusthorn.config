package net.cactusthorn.config.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class LoadTypeTest {

    @Test public void merge() {
        TestConfig testConfig = ConfigFactory.create(TestConfig.class, "classpath:config/testconfig.properties",
                "classpath:config/testconfig2.properties");
        assertEquals("SSSSSSSS", testConfig.dstr());
    }

    @Test public void first() {
        TestConfig testConfig = ConfigFactory.builder().setLoadPolicy(Config.LoadType.FIRST)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").build()
                .create(TestConfig.class);
        assertEquals("A", testConfig.dstr());
    }

    @Test public void withMapMerge() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.dstr", "FROMMAP");
        TestConfig testConfig = ConfigFactory.builder().setLoadPolicy(Config.LoadType.MERGE)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").setSource(properties)
                .build().create(TestConfig.class);
        assertEquals("FROMMAP", testConfig.dstr());
    }

    @Test public void withMapFirst() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.dstr", "FROMMAP");
        TestConfig testConfig = ConfigFactory.builder().setLoadPolicy(Config.LoadType.FIRST)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").setSource(properties)
                .build().create(TestConfig.class);
        assertEquals("FROMMAP", testConfig.dstr());
    }

    @Test public void firstSkipEmpty() {
        TestConfig testConfig = ConfigFactory.builder().setLoadPolicy(Config.LoadType.FIRST)
                .addSource("classpath:config/notExists.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("A", testConfig.dstr());
    }

    @Test public void firstNotExists() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setLoadPolicy(Config.LoadType.FIRST)
                .addSource("classpath:config/notExists.properties").build().create(TestConfig.class));
    }
}
