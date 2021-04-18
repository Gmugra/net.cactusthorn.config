package net.cactusthorn.config.tests.key;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class WithPrefixTest {

    private static WithPrefix config;

    @BeforeAll static void setUp() {
        Map<String, String> properties = new HashMap<>();
        properties.put("prefix.simple", "ABC");
        properties.put("prefix.abc.withKey", "XYZ");

        config = ConfigFactory.create(WithPrefix.class, properties);
    }

    @Test public void simple() {
        assertEquals("ABC", config.simple());
    }

    @Test public void withKey() {
        assertEquals("XYZ", config.withKey());
    }
}
