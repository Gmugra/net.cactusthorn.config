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
package net.cactusthorn.config.tests.converter;

import static org.junit.jupiter.api.Assertions.*;
import static java.util.Optional.of;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

class ConfigParamConverterTest {

    @Test void convert() {
        var properties = Map.of("localDate", "12.11.2011");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(LocalDate.of(2011, 11, 12), config.localDate());
    }

    @Test void convertDT() {
        var properties = Map.of("localDate", "12.11.2011", "localDateTime", "10.03.2016 08:11:33");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(of(LocalDateTime.of(2016, 3, 10, 8, 11, 33)), config.localDateTime());
    }

    @Test void convertZDT() {
        var properties = Map.of("localDate", "12.11.2011", "zonedDateTime", "10.03.2016 08:11:33Z");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertTrue(config.zonedDateTime().isPresent());
    }

    @Test void localDateDefault() {
        var properties = Map.of("localDate", "12.11.2011", "localDateDefault", "2011-11-12");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(of(LocalDate.of(2011, 11, 12)), config.localDateDefault());
    }

    @Test void offsetDateTime() {
        var properties = Map.of("localDate", "12.11.2011", "offsetDateTime", "12.11.2011T01:30:00+02:00");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(of(OffsetDateTime.of(2011, 11, 12, 1, 30, 0, 0, ZoneOffset.of("+02:00"))), config.offsetDateTime());
    }

    @Test void localTime() {
        var properties = Map.of("localDate", "12.11.2011", "localTime", "01:30:00");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(of(LocalTime.of(1, 30, 0)), config.localTime());
    }

    @Test void year() {
        var properties = Map.of("localDate", "12.11.2011", "year", "B:2016");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(of(Year.of(2016)), config.year());
    }

    @Test void yearMonth() {
        var properties = Map.of("localDate", "12.11.2011", "yearMonth", "2016:11");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(of(YearMonth.of(2016, 11)), config.yearMonth());
    }

    @Test void offsetTime() {
        var properties = Map.of("localDate", "12.11.2011", "offsetTime", "01-30-00+02:00");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(of(OffsetTime.of(1, 30, 0, 0, ZoneOffset.of("+02:00"))), config.offsetTime());
    }

    @Test void monthDay() {
        var properties = Map.of("localDate", "12.11.2011", "monthDay", "01-15");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(of(MonthDay.of(1, 15)), config.monthDay());
    }
}
