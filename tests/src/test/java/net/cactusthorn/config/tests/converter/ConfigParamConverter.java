package net.cactusthorn.config.tests.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.converter.ConverterLocalDate;
import net.cactusthorn.config.core.converter.ConverterLocalDateTime;
import net.cactusthorn.config.core.converter.ConverterZonedDateTime;

@Config public interface ConfigParamConverter {

    @ConverterLocalDate({"dd.MM.yyyy", "yyyy-MM-dd"}) LocalDate localDate();

    @ConverterLocalDateTime({"dd.MM.yyyy' 'HH:mm:ss"}) Optional<LocalDateTime> localDateTime();

    @ConverterZonedDateTime({"dd.MM.yyyy' 'HH:mm:sszzz"}) Optional<ZonedDateTime> zonedDateTime();
}
