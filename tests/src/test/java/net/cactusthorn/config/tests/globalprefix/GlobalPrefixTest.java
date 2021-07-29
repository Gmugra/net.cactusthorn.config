package net.cactusthorn.config.tests.globalprefix;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

public class GlobalPrefixTest {

    @Test public void disableMethod() {
        System.clearProperty("xxx");
        Map<String, String> properties = new HashMap<>();
        properties.put("value", "ABC");
        properties.put("gp.gpValue", "XYZ");
        GPDisableMethod config = ConfigFactory.builder().setSource(properties).setGlobalPrefix("gp").build().create(GPDisableMethod.class);
        assertEquals("ABC", config.value());
        assertEquals("XYZ", config.gpValue());
    }

    @Test public void disableMethodEnv() {
        System.setProperty("xxx", "dev");
        Map<String, String> properties = new HashMap<>();
        properties.put("value", "ABC");
        properties.put("gp.dev.gpValue", "XYZ");
        GPDisableMethod config = ConfigFactory.builder().setSource(properties).setGlobalPrefix("gp").build().create(GPDisableMethod.class);
        assertEquals("ABC", config.value());
        assertEquals("XYZ", config.gpValue());
    }

    @Test public void disableAll() {
        System.clearProperty("xxx");
        Map<String, String> properties = new HashMap<>();
        properties.put("value", "ABC");
        properties.put("gpValue", "XYZ");
        GPDisableAll config = ConfigFactory.builder().setSource(properties).setGlobalPrefix("gp").build().create(GPDisableAll.class);
        assertEquals("ABC", config.value());
        assertEquals("XYZ", config.gpValue());
    }

    @Test public void gpNotSet() {
        System.clearProperty("xxx");
        Map<String, String> properties = new HashMap<>();
        properties.put("value", "ABC");
        properties.put("gpValue", "XYZ");
        GPDisableMethod config = ConfigFactory.builder().setSource(properties).build().create(GPDisableMethod.class);
        assertEquals("ABC", config.value());
        assertEquals("XYZ", config.gpValue());
    }
}
