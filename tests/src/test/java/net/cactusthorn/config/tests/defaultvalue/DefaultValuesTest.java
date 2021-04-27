package net.cactusthorn.config.tests.defaultvalue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class DefaultValuesTest {

    private static DefaultValues config;

    @BeforeAll static void setUp() {
        config = ConfigFactory.builder().build().create(DefaultValues.class);
    }

    @Test public void str() {
        assertEquals("string", config.str());
    }

    @Test public void list() {
        assertEquals(3, config.list().size());
        assertEquals("A", config.list().get(0));
        assertEquals("B", config.list().get(1));
        assertEquals("C", config.list().get(2));
    }

    @Test public void set() {
        assertEquals(2, config.set().size());
    }

    @Test public void sorted() {
        assertEquals(2, config.sorted().size());
        assertEquals("B", config.sorted().iterator().next());
    }
}
