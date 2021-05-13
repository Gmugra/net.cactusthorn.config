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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.cactusthorn.config.core.converter.Converter;

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
