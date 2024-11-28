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
package net.cactusthorn.config.tests.defaultConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.bytesize.ByteSize;
import net.cactusthorn.config.core.converter.bytesize.ByteSizeUnit;
import net.cactusthorn.config.core.factory.ConfigFactory;

class DefaultConvertersTest {

    private static DefaultConverters config;

    @BeforeAll static void init() {
        var properties = new HashMap<String, String>();
        properties.put("character", "ABC");
        properties.put("url", "https://github.com");
        properties.put("uri", "https://gmail.com");
        properties.put("path", "my.xml");
        properties.put("currency", "EUR");
        properties.put("byteSize", "10 bytes");
        properties.put("instant", "2007-12-03T10:15:30.00Z");
        properties.put("duration", "PT15M");
        properties.put("period", "10 days");
        properties.put("localDate", "2016-03-10");
        properties.put("localDateTime", "2016-03-10T08:11:33");
        properties.put("localTime", "08:11:33");
        properties.put("zonedDateTime", "2016-03-10T08:11:33Z");
        properties.put("offsetDateTime", "2011-11-12T01:30:00+02:00");
        properties.put("offsetTime", "01:30:00+02:00");
        properties.put("year", "2016");
        properties.put("yearMonth", "2016-11");
        properties.put("locale", "de-DE");
        properties.put("pattern", "[^\\w]");
        properties.put("monthDay", "--01-15");
        properties.put("zoneId", "GMT+02:00");
        properties.put("zoneOffset", "+02:00");
        config = ConfigFactory.builder().setSource(properties).build().create(DefaultConverters.class);
    }

    @Test void character() throws MalformedURLException {
        assertEquals(Character.valueOf('A'), config.character());
    }

    @Test void url() throws MalformedURLException {
        assertEquals(new URL("https://github.com"), config.url());
    }

    @Test void uri() throws MalformedURLException {
        assertEquals(URI.create("https://gmail.com"), config.uri());
    }

    @Test void path() throws MalformedURLException {
        assertEquals(Paths.get("my.xml"), config.path());
    }

    @Test void currency() {
        assertEquals(Currency.getInstance("EUR"), config.currency());
    }

    @Test void locale() {
        assertEquals(Locale.GERMANY, config.locale());
    }

    @Test void instant() {
        assertEquals(Instant.parse("2007-12-03T10:15:30.00Z"), config.instant());
    }

    @Test void duration() {
        assertEquals(Duration.of(15, ChronoUnit.MINUTES), config.duration());
    }

    @Test void period() {
        assertEquals(Period.ofDays(10), config.period());
    }

    @Test void localDate() {
        assertEquals(LocalDate.of(2016, 3, 10), config.localDate());
    }

    @Test void localDateTime() {
        assertEquals(LocalDateTime.of(2016, 3, 10, 8, 11, 33), config.localDateTime());
    }

    @Test void localTime() {
        assertEquals(LocalTime.of(8, 11, 33), config.localTime());
    }

    @Test void zonedDateTime() {
        assertEquals(Instant.parse("2016-03-10T08:11:33Z"), config.zonedDateTime().toInstant());
    }

    @Test void offsetDateTime() {
        assertEquals(OffsetDateTime.of(2011, 11, 12, 1, 30, 0, 0, ZoneOffset.of("+02:00")), config.offsetDateTime());
    }

    @Test void offsetTime() {
        assertEquals(OffsetTime.of(1, 30, 0, 0, ZoneOffset.of("+02:00")), config.offsetTime());
    }

    @Test void year() {
        assertEquals(Year.of(2016), config.year());
    }

    @Test void yearMonth() {
        assertEquals(YearMonth.of(2016, 11), config.yearMonth());
    }

    @Test void byteSize() {
        assertEquals(new ByteSize(10, ByteSizeUnit.BYTES), config.byteSize());
    }

    @Test void pattern() {
        assertEquals("[^\\w]", config.pattern().pattern());
    }

    @Test void monthDay() {
        assertEquals(MonthDay.of(1, 15), config.monthDay());
    }

    @Test void zoneId() {
        assertEquals(ZoneId.of("GMT+02:00"), config.zoneId());
    }

    @Test void zoneOffset() {
        assertEquals(ZoneOffset.of("+02:00"), config.zoneOffset());
    }
}
