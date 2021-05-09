package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class CustomLoaderTest {

    public static final class TestLoader implements Loader {
        @Override public boolean accept(URI uri) {
            return uri.toString().equals("system:properties");
        }

        @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
            Map<String, String> result = new HashMap<>();
            result.put("testKey", "FromTestLoader");
            return result;
        }
    }

    static final class NotPublic implements Loader {
        @Override public boolean accept(URI uri) {
            return uri.toString().equals("system:properties");
        }

        @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
            return null;
        }
    }

    @Test public void standard() {
        System.setProperty("testKey", "realValue");
        ConfigHolder holder = ConfigFactory.builder().addSourceNoCache("system:properties").build().configHolder();
        assertEquals("realValue", holder.getString("testKey"));
    }

    @Test public void custom() {
        System.setProperty("testKey", "realValue");
        ConfigHolder holder = ConfigFactory.builder().addLoader(new TestLoader()).addSourceNoCache("system:properties").build()
                .configHolder();
        assertEquals("FromTestLoader", holder.getString("testKey"));
    }

    @Test public void customAsClass() {
        System.setProperty("testKey", "realValue");
        ConfigHolder holder = ConfigFactory.builder().addLoader(TestLoader.class).addSourceNoCache("system:properties").build()
                .configHolder();
        assertEquals("FromTestLoader", holder.getString("testKey"));
    }

    @Test public void cantInvoke() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addLoader(NotPublic.class));
    }

    @Test public void addLoaderNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addLoader((Loader) null));
    }

    @Test public void addLoaderClassNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addLoader((Class<? extends Loader>) null));
    }
}
