/*
 * Copyright (C) 2021, Alexei Khatskevich
 *
 * Licensed under the BSD 3-Clause license.
 * You may obtain a copy of the License at
 *
 * https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.cactusthorn.config.core.converter.standard;

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.util.NumericAndCharSplitter;

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

    private static final NumericAndCharSplitter SPLITTER = new NumericAndCharSplitter();

    @Override public Duration convert(String value, String[] parameters) {
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
        String[] parts = SPLITTER.split(input);
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
}
