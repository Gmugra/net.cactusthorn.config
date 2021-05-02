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

public class ClasspathXMLLoaderTest {

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    private static final Loader LOADER = new ClasspathXMLLoader();
    private static final ClassLoader CL = ClasspathXMLLoaderTest.class.getClassLoader();

    @Test public void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("classpath:standard-properties.xml")));
    }

    @Test public void acceptFragment() {
        assertTrue(LOADER.accept(URI.create("classpath:standard-properties.xml#ISO-8859-1")));
    }

    @Test public void notAcceptNotOpaque() {
        assertFalse(LOADER.accept(URI.create("classpath://a.xml#ISO-8859-1")));
    }

    @Test public void notAcceptNotXml() {
        assertFalse(LOADER.accept(URI.create("classpath:a.properties#ISO-8859-1")));
    }

    @Test public void notAcceptScheme() {
        assertFalse(LOADER.accept(URI.create("mail:a.xml#ISO-8859-1")));
    }

    @Test public void load() {
        Map<String, String> properties = LOADER.load(URI.create("classpath:standard-properties.xml#UTF-8"), CL);
        assertEquals("foobar", properties.get("server.http.hostname"));
    }

    @Test public void loadWrong() {
        Map<String, String> properties = LOADER.load(URI.create("classpath:standard-properties-wrong.xml"), CL);
        assertTrue(properties.isEmpty());
    }

    @Test public void loadOwner() {
        Map<String, String> properties = LOADER.load(URI.create("classpath:owner.xml"), CL);
        assertEquals("localhost", properties.get("server.http.hostname"));
    }

    @Test public void loadOwnerSpecial() {
        Map<String, String> properties = LOADER.load(URI.create("classpath:owner-special.xml"), CL);
        assertEquals("å∫ç∂´ƒ©˙ˆ∆ü", properties.get("foo.baz.specialChars"));
    }
}
