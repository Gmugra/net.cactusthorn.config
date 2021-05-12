package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class LocalDateConverterTest {

    static final LocalDate DATE = LocalDate.of(2016, 3, 10);
    static final Converter<LocalDate> CONVERTER = new LocalDateConverter();

    @Test public void simple() {
        LocalDate result = CONVERTER.convert("2016-03-10");
        assertEquals(DATE, result);
    }

    @Test public void simpleNullParameters() {
        LocalDate result = CONVERTER.convert("2016-03-10", null);
        assertEquals(DATE, result);
    }

    @Test public void simpleEmptyParameters() {
        LocalDate result = CONVERTER.convert("2016-03-10", new String[0]);
        assertEquals(DATE, result);
    }

    @Test public void complex() {
        LocalDate result = CONVERTER.convert("10.03.2016", new String[] { "yyyy-MM-dd", "dd.MM.yyyy" });
        assertEquals(DATE, result);
    }

    @Test public void singleParam() {
        LocalDate result = CONVERTER.convert("10.03.2016", new String[] { "dd.MM.yyyy" });
        assertEquals(DATE, result);
    }
}
