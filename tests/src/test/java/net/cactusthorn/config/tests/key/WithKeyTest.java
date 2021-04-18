package net.cactusthorn.config.tests.key;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class WithKeyTest {

    private static WithKey config;

    @BeforeAll static void setUp() {
        Map<String, String> properties = new HashMap<>();
        properties.put("simple", "ABC");
        properties.put("abc.withKey", "XYZ");

        config = ConfigFactory.create(WithKey.class, properties);
    }

    @Test public void simple() {
        assertEquals("ABC", config.simple());
    }

    @Test public void withKey() {
        assertEquals("XYZ", config.withKey());
    }
}
