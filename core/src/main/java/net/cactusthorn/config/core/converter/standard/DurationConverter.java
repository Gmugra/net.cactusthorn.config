package net.cactusthorn.config.core.converter.standard;

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import net.cactusthorn.config.core.converter.Converter;

/**
 *
 * This converter will convert various duration formatted strings over to
 * {@link java.time.Duration} objects.
 *
 * The class supports two formats for the duration string:
 * <ul>
 * <li>The ISO-8601 based format that the
 * {@link java.time.Duration#parse(CharSequence)} method supports (<a href=
 * "https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-"
 * target="_blank">see the official Java 8 documentation</a>, although note that
 * currently there is an
 * <a href="https://bugs.openjdk.java.net/browse/JDK-8146173" target=
 * "_blank">error in the documentation</a>). The implementation will check
 * whether the input string starts with <code>P</code> with an optional
 * plus/minus prefix and if so, will use this method for parsing.</li>
 * <li>A "<code>value time_unit</code>" string where the <code>value</code> is
 * an integer and <code>time_unit</code> is one of:
 * <ul>
 * <li>ns / nanos / nanoseconds</li>
 * <li>us / µs / micros / microseconds</li>
 * <li>ms / millis / milliseconds</li>
 * <li>s / seconds</li>
 * <li>m / minutes</li>
 * <li>h / hours</li>
 * <li>d / days</li>
 * </ul>
 * <p>
 * Note that the <code>time_unit</code> string is case sensitive.
 * <p>
 * If no <code>time_unit</code> is specified, <code>milliseconds</code> is
 * assumed.</li>
 * </ul>
 *
 * @author Luigi R. Viggiano, Alexei Khatskevich
 */
public class DurationConverter implements Converter<Duration> {

    @Override public Duration convert(String value) {
        // If it looks like a string that Duration.parse can handle, let's try that.
        if (value.startsWith("P") || value.startsWith("-P") || value.startsWith("+P")) {
            return Duration.parse(value);
        }
        // ...otherwise we'll perform our own parsing
        return parseDuration(value);
    }

    /**
     * Parses a duration string. If no units are specified in the string, it is
     * assumed to be in milliseconds.
     *
     * This implementation was blatantly stolen/adapted from the typesafe-config
     * project:
     * https://github.com/typesafehub/config/blob/v1.3.0/config/src/main/java/com/typesafe/config/impl/SimpleConfig.java#L551-L624
     *
     * @param input the string to parse
     * @return duration
     * @throws IllegalArgumentException if input is invalid
     */
    private Duration parseDuration(String input) {
        String[] parts = splitNumericAndChar(input);
        String numberString = parts[0];
        String originalUnitString = parts[1];
        String unitString = originalUnitString;

        if (numberString.length() == 0) {
            throw new IllegalArgumentException(msg(DURATION_NO_NUMBER, input));
        }

        if (unitString.length() > 2 && !unitString.endsWith("s")) {
            unitString = unitString + 's';
        }

        ChronoUnit units;
        // note that this is deliberately case-sensitive
        switch (unitString) {
        case "ns":
        case "nanos":
        case "nanoseconds":
            units = ChronoUnit.NANOS;
            break;
        case "us":
        case "µs":
        case "micros":
        case "microseconds":
            units = ChronoUnit.MICROS;
            break;
        case "":
        case "ms":
        case "millis":
        case "milliseconds":
            units = ChronoUnit.MILLIS;
            break;
        case "s":
        case "seconds":
            units = ChronoUnit.SECONDS;
            break;
        case "m":
        case "minutes":
            units = ChronoUnit.MINUTES;
            break;
        case "h":
        case "hours":
            units = ChronoUnit.HOURS;
            break;
        case "d":
        case "days":
            units = ChronoUnit.DAYS;
            break;
        default:
            throw new IllegalArgumentException(msg(DURATION_WRONG_TIME_UNIT, originalUnitString));
        }

        return Duration.of(Long.parseLong(numberString), units);
    }

    /**
     * Splits a string into a numeric part and a character part. The input string
     * should conform to the format <code>[numeric_part][char_part]</code> with an
     * optional whitespace between the two parts.
     *
     * The <code>char_part</code> should only contain letters as defined by
     * {@link Character#isLetter(char)} while the <code>numeric_part</code> will be
     * parsed regardless of content.
     *
     * Any whitespace will be trimmed from the beginning and end of both parts,
     * however, the <code>numeric_part</code> can contain whitespaces within it.
     *
     * @param input the string to split.
     *
     * @return an array of two strings.
     */
    static String[] splitNumericAndChar(String input) {
        // ATTN: String.trim() may not trim all UTF-8 whitespace characters properly.
        // The original implementation used its own unicodeTrim() method that I decided
        // not to include until the need
        // arises. For more information, see:
        // https://github.com/typesafehub/config/blob/v1.3.0/config/src/main/java/com/typesafe/config/impl/ConfigImplUtil.java#L118-L164

        int i = input.length() - 1;
        while (i >= 0) {
            char c = input.charAt(i);
            if (!Character.isLetter(c)) {
                break;
            }
            i -= 1;
        }
        return new String[] {input.substring(0, i + 1).trim(), input.substring(i + 1).trim()};
    }
}
