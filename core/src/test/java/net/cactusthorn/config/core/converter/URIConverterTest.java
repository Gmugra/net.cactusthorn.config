package net.cactusthorn.config.core.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URI;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.standard.URIConverter;

public class URIConverterTest {

    static Converter<URI> converter = new URIConverter();

    @Test public void correct() throws MalformedURLException {
        URI uri = converter.convert("https://github.com");
        assertEquals(URI.create("https://github.com"), uri);
    }
}
