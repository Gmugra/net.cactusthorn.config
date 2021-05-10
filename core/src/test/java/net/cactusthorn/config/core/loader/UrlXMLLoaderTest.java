package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.io.TempDir;

import net.cactusthorn.config.core.loader.standard.UrlXMLLoader;

public class UrlXMLLoaderTest {

    private static final Loader LOADER = new UrlXMLLoader();
    private static final ClassLoader CL = UrlXMLLoaderTest.class.getClassLoader();

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("file:./a.xml")));
    }

    @Test public void notAcceptExtention() {
        assertFalse(LOADER.accept(URI.create("file:./a.properties")));
    }

    @Test public void notAcceptException() {
        assertFalse(LOADER.accept(URI.create("github.com/a.xml")));
    }

    @Test public void notAcceptException2() {
        assertFalse(LOADER.accept(URI.create("system:a.xml")));
    }

    @Test public void load(@TempDir Path path) throws IOException {
        Path file = path.resolve("standard-properties.xml");
        try (InputStream stream = CL.getResourceAsStream("standard-properties.xml")) {
            Files.copy(stream, file);
        }
        URI uri = file.toUri();
        Map<String, String> properties = LOADER.load(uri, CL);
        assertEquals("foobar", properties.get("server.http.hostname"));
    }

    @Test public void loadFragment(@TempDir Path path) throws IOException, URISyntaxException {
        Path file = path.resolve("standard-properties.xml");
        try (InputStream stream = CL.getResourceAsStream("standard-properties.xml")) {
            Files.copy(stream, file);
        }
        URI uri = file.toUri();
        uri = new URI(uri.getScheme(), uri.getSchemeSpecificPart(), "UTF-8");
        Map<String, String> properties = LOADER.load(uri, CL);
        assertEquals("foobar", properties.get("server.http.hostname"));
    }

    @Test public void notLoad() throws IOException {
        Map<String, String> properties = LOADER.load(URI.create("file:./a.xml"), CL);
        assertTrue(properties.isEmpty());
    }
}
