package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.io.TempDir;

public class UrlPropertiesLoaderTest {

    private static final Loader LOADER = new UrlPropertiesLoader();
    private static final ClassLoader CL = UrlPropertiesLoaderTest.class.getClassLoader();

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("file:./a.properties")));
    }

    @Test public void notAcceptExtention() {
        assertFalse(LOADER.accept(URI.create("file:./a.xml")));
    }

    @Test public void notAcceptException() {
        assertFalse(LOADER.accept(URI.create("classpath:a.xml")));
    }

    @Test public void notAcceptException2() {
        assertFalse(LOADER.accept(URI.create("system:a.xml")));
    }

    @Test public void load(@TempDir Path path) throws IOException {
        Path file = path.resolve("test.properties");
        try (InputStream stream = CL.getResourceAsStream("test.properties")) {
            Files.copy(stream, file);
        }
        URI uri = file.toUri();
        Map<String, String> properties = LOADER.load(uri, CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Test public void loadFragment(@TempDir Path path) throws IOException, URISyntaxException {
        Path file = path.resolve("test.properties");
        try (InputStream stream = CL.getResourceAsStream("test.properties")) {
            Files.copy(stream, file);
        }
        URI uri = file.toUri();
        uri = new URI(uri.getScheme(), uri.getSchemeSpecificPart(), "UTF-8");
        Map<String, String> properties = LOADER.load(uri, CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Disabled @Test public void loadGithub(@TempDir Path path) throws IOException, URISyntaxException {
        URI uri = URI.create(
                "https://raw.githubusercontent.com/Gmugra/net.cactusthorn.config/main/core/src/test/resources/test.properties#UTF-8");
        Map<String, String> properties = LOADER.load(uri, CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Test public void notLoad() throws IOException {
        Map<String, String> properties = LOADER.load(URI.create("file:./a.properties"), CL);
        assertTrue(properties.isEmpty());
    }
}
