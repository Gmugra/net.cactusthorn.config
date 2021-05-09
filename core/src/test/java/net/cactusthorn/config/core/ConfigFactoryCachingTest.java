package net.cactusthorn.config.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.ConfigHolder;

public class ConfigFactoryCachingTest {

    @Test public void cached() {
        ConfigFactory factory = ConfigFactory.builder().addSource("system:properties").build();
        
        System.setProperty("myKey", "firstValue");
        ConfigHolder holder = factory.configHolder();
        assertEquals("firstValue", holder.getString("myKey"));

        System.setProperty("myKey", "secondValue");
        holder = factory.configHolder();
        assertEquals("firstValue", holder.getString("myKey"));
    }

    @Test public void notCached() {
        ConfigFactory factory = ConfigFactory.builder().addSourceNoCache(URI.create("system:properties")).build();

        System.setProperty("myKey", "firstValue");
        ConfigHolder holder = factory.configHolder();
        assertEquals("firstValue", holder.getString("myKey"));

        System.setProperty("myKey", "secondValue");
        holder = factory.configHolder();
        assertEquals("secondValue", holder.getString("myKey"));
    }
}
