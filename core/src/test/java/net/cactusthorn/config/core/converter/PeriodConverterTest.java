package net.cactusthorn.config.core.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Period;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PeriodConverterTest {

    static Converter<Period> converter = new PeriodConverter();

    @Test public void iso8601() {
        assertEquals(Period.ofYears(2), converter.convert("P2Y"));
        assertEquals(Period.ofYears(2), converter.convert("+P2Y"));
        assertEquals(Period.of(-1, -2, 0), converter.convert("-P1Y2M"));
    }

    @Test public void emptySuffix() {
        assertEquals(Period.ofDays(10), converter.convert("10"));
    }

    @Test public void noSpaceBeforeSuffix() {
        assertEquals(Period.ofDays(10), converter.convert("10day"));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 d", "10 day", "10 days" }) //
    public void daySuffixEquality(String value) {
        assertEquals(Period.ofDays(10), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 w", "10 week", "10 weeks" }) //
    public void weekSuffixEquality(String value) {
        assertEquals(Period.ofWeeks(10), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 m", "10 mo", "10 month", "10 months" }) //
    public void monthSuffixEquality(String value) {
        assertEquals(Period.ofMonths(10), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 y", "10 year", "10 years" }) //
    public void yearSuffixEquality(String value) {
        assertEquals(Period.ofYears(10), converter.convert(value));
    }

    @Test public void noNumberException() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("days"));
    }

    @Test public void wrongUnitException() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("10 x"));
    }
}
