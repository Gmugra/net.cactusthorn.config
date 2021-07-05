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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Optional;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.converter.LocalDateParser;
import net.cactusthorn.config.core.converter.LocalDateTimeParser;
import net.cactusthorn.config.core.converter.LocalTimeParser;
import net.cactusthorn.config.core.converter.OffsetDateTimeParser;
import net.cactusthorn.config.core.converter.OffsetTimeParser;
import net.cactusthorn.config.core.converter.YearMonthParser;
import net.cactusthorn.config.core.converter.YearParser;
import net.cactusthorn.config.core.converter.ZonedDateTimeParser;

@Config public interface ConfigParamConverter {

    @LocalDateParser({"dd.MM.yyyy", "yyyy-MM-dd"}) LocalDate localDate();

    @LocalDateTimeParser({"dd.MM.yyyy' 'HH:mm:ss"}) Optional<LocalDateTime> localDateTime();

    @ZonedDateTimeParser({"dd.MM.yyyy' 'HH:mm:sszzz"}) Optional<ZonedDateTime> zonedDateTime();

    @OffsetDateTimeParser({"yyyy-MM-dd'T'HH:mm:ssXXX", "dd.MM.yyyy'T'HH:mm:ssXXX"}) Optional<OffsetDateTime> offsetDateTime();

    @LocalTimeParser({"HH:mm:ss", "HH'-'mm'-'ss"}) Optional<LocalTime> localTime();

    @YearParser({"'A:'yyyy", "'B:'yyyy"}) Optional<Year> year();

    @YearMonthParser({"yyyy-MM", "yyyy:MM"}) Optional<YearMonth> yearMonth();

    @OffsetTimeParser({"HH:mm:ssXXX", "HH'-'mm'-'ssXXX"}) Optional<OffsetTime> offsetTime();

    Optional<LocalDate> localDateDefault();
}
