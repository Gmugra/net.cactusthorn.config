package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class SystemEnvLoaderTest {

    private static final Loader CPP = new SystemEnvLoader();
    private static final ClassLoader CL = SystemEnvLoaderTest.class.getClassLoader();
    private static final URI SEURI = URI.create("system:env");

    @Test public void acceptSimple() {
        assertTrue(CPP.accept(SEURI));
    }

    @Test public void notAcceptNotOpaque() {
        assertFalse(CPP.accept(URI.create("classpath://a.properties#ISO-8859-1")));
    }

    @Test public void notAcceptNotSystem() {
        assertFalse(CPP.accept(URI.create("classpath:a.properties")));
    }

    @Test public void notAcceptNotProperties() {
        assertFalse(CPP.accept(URI.create("system:properties")));
    }

    @Test public void load() {
        Map<String, String> values = CPP.load(SEURI, CL);
        assertFalse(values.isEmpty());
    }
}
