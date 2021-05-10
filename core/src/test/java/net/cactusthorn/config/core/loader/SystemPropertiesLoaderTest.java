package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.standard.SystemPropertiesLoader;

public class SystemPropertiesLoaderTest {

    private static final Loader LOADER = new SystemPropertiesLoader();
    private static final ClassLoader CL = SystemPropertiesLoaderTest.class.getClassLoader();
    private static final URI SPURI = URI.create("system:properties");

    @Test public void acceptSimple() {
        assertTrue(LOADER.accept(SPURI));
    }

    @Test public void notAcceptNotOpaque() {
        assertFalse(LOADER.accept(URI.create("classpath://a.properties#ISO-8859-1")));
    }

    @Test public void notAcceptNotSystem() {
        assertFalse(LOADER.accept(URI.create("classpath:a.properties")));
    }

    @Test public void notAcceptNotProperties() {
        assertFalse(LOADER.accept(URI.create("system:abc")));
    }

    @Test public void load() {
        System.setProperty("TEST", "TESTVALUE");
        Map<String, String> values = LOADER.load(SPURI, CL);
        assertEquals("TESTVALUE", values.get("TEST"));
    }
}
