package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClasspathJarManifestLoaderTest {

    private static final Loader LOADER = new ClasspathJarManifestLoader();
    private static final ClassLoader CL = ClasspathJarManifestLoaderTest.class.getClassLoader();

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void accept() {
        assertTrue(LOADER.accept(URI.create("classpath:jar:manifest?a=b")));
    }

    @Test public void notAccept() {
        assertFalse(LOADER.accept(URI.create("classpath:a.xml#ISO-8859-1")));
    }

    @Test public void notFoundNameValue() {
        LOADER.load(URI.create("classpath:jar:manifest?a=b"), CL);
    }

    @Test public void notFoundName() {
        LOADER.load(URI.create("classpath:jar:manifest?a"), CL);
    }

    @Test public void load() {
        Map<String, String> result = LOADER.load(URI.create("classpath:jar:manifest?Bundle-Name=JUnit%20Jupiter%20API"), CL);
        assertEquals("junit-jupiter-api", result.get("Implementation-Title"));
    }

    @Test public void loadOnlyName() {
        Map<String, String> result = LOADER.load(URI.create("classpath:jar:manifest?Bundle-Name"), CL);
        assertFalse(result.isEmpty());
    }
}
