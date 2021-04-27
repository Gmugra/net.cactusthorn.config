package net.cactusthorn.config.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class ConfigFactoryTest {

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
        properties.put("myChar", "YXZ");
        config = ConfigFactory.builder().setSource(properties).build().create(AllCorrect.class);
    }

    @Test public void myChar() {
        assertEquals('Y', config.myChar());
    }

    @Test public void sorted() {
        assertEquals(2, config.sorted().size());
        Iterator<Float> it = config.sorted().iterator();
        assertEquals(126f, it.next());
        assertEquals(300f, it.next());
    }
}
