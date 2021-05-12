package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class URLConverterTest {

    static Converter<URL> converter = new URLConverter();

    @Test public void correct() throws MalformedURLException {
        URL url = converter.convert("https://github.com");
        assertEquals(new URL("https://github.com"), url);
    }

    @Test public void wrong() throws MalformedURLException {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("github.com"));
    }
}
