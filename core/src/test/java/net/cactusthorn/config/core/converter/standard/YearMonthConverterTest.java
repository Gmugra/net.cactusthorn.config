package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class YearMonthConverterTest {

    private static final YearMonth YEAR_MONTH = YearMonth.of(2016, 11);
    private static final Converter<YearMonth> CONVERTER = new YearMonthConverter();

    @Test public void simple() {
        YearMonth result = CONVERTER.convert("2016-11");
        assertEquals(YEAR_MONTH, result);
    }

    @Test public void simpleNullParameters() {
        YearMonth result = CONVERTER.convert("2016-11", null);
        assertEquals(YEAR_MONTH, result);
    }

    @Test public void simpleEmptyParameters() {
        YearMonth result = CONVERTER.convert("2016-11", new String[0]);
        assertEquals(YEAR_MONTH, result);
    }

    @Test public void complex() {
        YearMonth result = CONVERTER.convert("2016:11", new String[] {"yyyy-MM", "yyyy:MM"});
        assertEquals(YEAR_MONTH, result);
    }

    @Test public void singleParam() {
        YearMonth result = CONVERTER.convert("2016:11", new String[] {"yyyy:MM"});
        assertEquals(YEAR_MONTH, result);
    }
}
