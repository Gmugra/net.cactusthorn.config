package net.cactusthorn.config.core.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.cactusthorn.config.core.converter.standard.DurationConverter;

public class DurationConverterTest {

    static Converter<Duration> converter = new DurationConverter();

    @Test public void emptySuffix() {
        assertEquals(Duration.of(10, ChronoUnit.MILLIS), converter.convert("10"));
    }

    @Test public void noSpaceBeforeSuffix() {
        assertEquals(Duration.of(10, ChronoUnit.MILLIS), converter.convert("10ms"));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 ns", "10 nano", "10 nanos", "10 nanosecond", "10 nanoseconds" }) //
    public void nanoSuffixEquality(String value) {
        assertEquals(Duration.of(10, ChronoUnit.NANOS), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 us", "10 µs", "10 micro", "10 micros", "10 microsecond", "10 microseconds" }) //
    public void microSuffixEquality(String value) {
        assertEquals(Duration.of(10, ChronoUnit.MICROS), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 ms", "10 milli", "10 millis", "10 millisecond", "10 milliseconds" }) //
    public void milliSuffixEquality(String value) {
        assertEquals(Duration.of(10, ChronoUnit.MILLIS), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 s", "10 second", "10 seconds" }) //
    public void secondSuffixEquality(String value) {
        assertEquals(Duration.of(10, ChronoUnit.SECONDS), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 m", "10 minute", "10 minutes" }) //
    public void minuteSuffixEquality(String value) {
        assertEquals(Duration.of(10, ChronoUnit.MINUTES), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 h", "10 hour", "10 hours" }) //
    public void hourSuffixEquality(String value) {
        assertEquals(Duration.of(10, ChronoUnit.HOURS), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10 d", "10 day", "10 days" }) //
    public void daySuffixEquality(String value) {
        assertEquals(Duration.of(10, ChronoUnit.DAYS), converter.convert(value));
    }

    @Test public void iso8601() {
        assertEquals(Duration.of(15, ChronoUnit.MINUTES), converter.convert("PT15M"));
        assertEquals(Duration.of(15, ChronoUnit.MINUTES), converter.convert("+PT15M"));
        assertEquals(Duration.of(-15, ChronoUnit.MINUTES), converter.convert("-PT15M"));
        assertEquals(Duration.of(6, ChronoUnit.HOURS).minus(3, ChronoUnit.MINUTES), converter.convert("-PT-6H+3M"));
    }

    @Test public void noNumberException() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("minute"));
    }

    @Test public void wrongUnitException() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("10 x"));
    }
}
