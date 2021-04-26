package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;
import net.cactusthorn.config.core.ConfigHolder;

public class CustomLoaderTest {

    static final class TestLoader implements Loader {
        @Override public boolean accept(URI uri) {
            return uri.toString().equals("system:properties");
        }

        @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
            Map<String, String> result = new HashMap<>();
            result.put("testKey", "FromTestLoader");
            return result;
        }
    }

    @Test public void standard() {
        System.setProperty("testKey", "realValue");
        ConfigHolder holder = ConfigFactory.builder().addSource("system:properties").build().configHolder();
        assertEquals("realValue", holder.getString("testKey"));
    }

    @Test public void custom() {
        System.setProperty("testKey", "realValue");
        ConfigHolder holder = ConfigFactory.builder().addLoader(new TestLoader()).addSource("system:properties").build().configHolder();
        assertEquals("FromTestLoader", holder.getString("testKey"));
    }
}
