package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClasspathPropertiesLoaderTest {

    private static final Loader CPP = new ClasspathPropertiesLoader();
    private static final ClassLoader CL = ClasspathPropertiesLoaderTest.class.getClassLoader();

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void acceptSimple() {
        assertTrue(CPP.accept(URI.create("classpath:a.properties")));
    }

    @Test public void acceptFragment() {
        assertTrue(CPP.accept(URI.create("classpath:a.properties#ISO-8859-1")));
    }

    @Test public void notAcceptNotOpaque() {
        assertFalse(CPP.accept(URI.create("classpath://a.properties#ISO-8859-1")));
    }

    @Test public void notAcceptNotProperties() {
        assertFalse(CPP.accept(URI.create("classpath:a.xml#ISO-8859-1")));
    }

    @Test public void notAcceptScheme() {
        assertFalse(CPP.accept(URI.create("mail:a.xml#ISO-8859-1")));
    }

    @Test public void load() throws IOException {
        Map<String, String> properties = CPP.load(URI.create("classpath:test.properties"), CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Test public void loadWithFragment() throws IOException {
        Map<String, String> properties = CPP.load(URI.create("classpath:test.properties#UTF-8"), CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Test public void notLoad() throws IOException {
        Map<String, String> properties = CPP.load(URI.create("classpath:notExists.properties"), CL);
        assertTrue(properties.isEmpty());
    }
}
