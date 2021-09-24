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

import java.time.Period;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.cactusthorn.config.core.converter.Converter;

public class PeriodConverterTest {

    private static Converter<Period> converter = new PeriodConverter();

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
