package net.cactusthorn.config.tests.split;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class SplitOnInterfaceTest {

    private static SplitOnInterface config;

    @BeforeAll static void setUp() {
        Map<String, String> properties = new HashMap<>();
        properties.put("list", "A:B;C");
        properties.put("list2", "Z,Z,Z");

        config = ConfigFactory.create(SplitOnInterface.class, properties);
    }

    @Test public void list() {
        assertEquals(3, config.list().size());
        assertEquals("A", config.list().get(0));
        assertEquals("B", config.list().get(1));
        assertEquals("C", config.list().get(2));
    }

    @Test public void list2() {
        assertEquals(1, config.list2().get().size());
        assertEquals("Z", config.list2().get().iterator().next());
    }
}
