package net.cactusthorn.config.core.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.standard.PathConverter;

public class PathConverterTest {

    static Converter<Path> converter = new PathConverter();

    @Test public void correct() throws MalformedURLException {
        assertEquals(Paths.get("my.xml"), converter.convert("my.xml"));
    }
}
