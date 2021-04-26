package net.cactusthorn.config.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class GonfigBuilderGeneratedTest {

    private static AllCorrect config;

    @BeforeAll static void setUp() {
        Map<String, String> properties = new HashMap<>();
        properties.put("buf", "ABC");
        properties.put("ddd", "125");
        properties.put("fromStringEnum", "xyz");
        properties.put("intValue", "124");
        // properties.put("list", "?"); is optional
        properties.put("set", "46400000-8cc0-11bd-b43e-10d46e4ef14d");
        properties.put("simpleEnum", "AAA");
        properties.put("sorted", "126,300");
        properties.put("superInterface", "SI");
        properties.put("uuid", "46400000-8cc0-11bd-b43e-10d46e4ef14d");
        properties.put("value", "simpleString");

        config = ConfigFactory.create(AllCorrect.class, properties);
    }

    @Test public void buf() {
        assertEquals("ABC", config.buf().toString());
    }

    @Test public void ddd() {
        assertEquals(125d, config.ddd());
    }

    @Test public void fromStringEnum() {
        assertEquals(AllCorrect.FromStringEnum.AAA, config.fromStringEnum());
    }

    @Test public void intValue() {
        assertEquals(124, config.intValue());
    }

    @Test public void list() {
        assertFalse(config.list().isPresent());
    }

    @Test public void set() {
        assertEquals(1, config.set().size());
        assertEquals("46400000-8cc0-11bd-b43e-10d46e4ef14d", config.set().iterator().next().toString());
    }

    @Test public void simpleEnum() {
        assertEquals(AllCorrect.SimpleEnum.AAA, config.simpleEnum());
    }

    @Test public void sorted() {
        assertEquals(2, config.sorted().size());
        Iterator<Float> it = config.sorted().iterator();
        assertEquals(126f, it.next());
        assertEquals(300f, it.next());
    }

    @Test public void uuid() {
        assertEquals("46400000-8cc0-11bd-b43e-10d46e4ef14d", config.uuid().get().toString());
    }

    @Test public void value() {
        assertEquals("simpleString", config.value());
    }
}
