package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class LocalDateTimeConverterTest {

    static final LocalDateTime DATE = LocalDateTime.of(2016, 3, 10, 8, 11, 33);
    static final Converter<LocalDateTime> CONVERTER = new LocalDateTimeConverter();

    @Test public void simple() {
        LocalDateTime result = CONVERTER.convert("2016-03-10T08:11:33");
        assertEquals(DATE, result);
    }

    @Test public void simpleNullParameters() {
        LocalDateTime result = CONVERTER.convert("2016-03-10T08:11:33", null);
        assertEquals(DATE, result);
    }

    @Test public void simpleEmptyParameters() {
        LocalDateTime result = CONVERTER.convert("2016-03-10T08:11:33", new String[0]);
        assertEquals(DATE, result);
    }

    @Test public void complex() {
        LocalDateTime result = CONVERTER.convert("10.03.2016 08:11:33", new String[] { "yyyy-MM-dd'T'HH:mm:ss", "dd.MM.yyyy' 'HH:mm:ss" });
        assertEquals(DATE, result);
    }

    @Test public void singleParam() {
        LocalDateTime result = CONVERTER.convert("2016-03-10T08:11:33", new String[] { "yyyy-MM-dd'T'HH:mm:ss" });
        assertEquals(DATE, result);
    }
}
