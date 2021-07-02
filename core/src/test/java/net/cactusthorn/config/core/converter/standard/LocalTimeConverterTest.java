package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class LocalTimeConverterTest {

    private static final LocalTime TIME = LocalTime.of(8, 11, 33);
    private static final Converter<LocalTime> CONVERTER = new LocalTimeConverter();

    @Test public void simple() {
        LocalTime result = CONVERTER.convert("08:11:33");
        assertEquals(TIME, result);
    }

    @Test public void simpleNullParameters() {
        LocalTime result = CONVERTER.convert("08:11:33", null);
        assertEquals(TIME, result);
    }

    @Test public void simpleEmptyParameters() {
        LocalTime result = CONVERTER.convert("08:11:33", new String[0]);
        assertEquals(TIME, result);
    }

    @Test public void complex() {
        LocalTime result = CONVERTER.convert("08-11-33", new String[] {"HH:mm:ss", "HH'-'mm'-'ss"});
        assertEquals(TIME, result);
    }

    @Test public void singleParam() {
        LocalTime result = CONVERTER.convert("08:11:33", new String[] {"HH:mm:ss"});
        assertEquals(TIME, result);
    }
}
