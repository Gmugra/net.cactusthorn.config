package net.cactusthorn.config.tests.split;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class SplitOnMethodTest {

    private static SplitOnMethod config;

    @BeforeAll static void setUp() {
        Map<String, String> properties = new HashMap<>();
        properties.put("list", "A,B,C");
        properties.put("list2", "X:Y;Z");

        config = ConfigFactory.create(SplitOnMethod.class, properties);
    }

    @Test public void list() {
        assertEquals(3, config.list().size());
        assertEquals("A", config.list().get(0));
        assertEquals("B", config.list().get(1));
        assertEquals("C", config.list().get(2));
    }

    @Test public void list2() {
        assertEquals(3, config.list2().get().size());
        assertEquals("X", config.list2().get().get(0));
        assertEquals("Y", config.list2().get().get(1));
        assertEquals("Z", config.list2().get().get(2));
    }
}
