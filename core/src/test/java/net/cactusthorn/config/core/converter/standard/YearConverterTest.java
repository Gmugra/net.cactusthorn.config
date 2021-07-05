package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Year;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class YearConverterTest {

    private static final Year YEAR = Year.of(2016);
    private static final Converter<Year> CONVERTER = new YearConverter();

    @Test public void simple() {
        Year result = CONVERTER.convert("2016");
        assertEquals(YEAR, result);
    }

    @Test public void simpleNullParameters() {
        Year result = CONVERTER.convert("2016", null);
        assertEquals(YEAR, result);
    }

    @Test public void simpleEmptyParameters() {
        Year result = CONVERTER.convert("2016", new String[0]);
        assertEquals(YEAR, result);
    }

    @Test public void complex() {
        Year result = CONVERTER.convert("16", new String[] {"yyyy", "yy"});
        assertEquals(YEAR, result);
    }

    @Test public void singleParam() {
        Year result = CONVERTER.convert("year: 16", new String[] {"'year: 'yy"});
        assertEquals(YEAR, result);
    }
}
