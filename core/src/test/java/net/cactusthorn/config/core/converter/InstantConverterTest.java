package net.cactusthorn.config.core.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class InstantConverterTest {

    static Converter<Instant> converter = new InstantConverter();

    @Test public void correct() throws MalformedURLException {
        Instant instant = converter.convert("2007-12-03T10:15:30.00Z");
        assertEquals(Instant.parse("2007-12-03T10:15:30.00Z"), instant);
    }
}
