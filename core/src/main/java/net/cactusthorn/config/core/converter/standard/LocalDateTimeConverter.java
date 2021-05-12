package net.cactusthorn.config.core.converter.standard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import net.cactusthorn.config.core.converter.Converter;

public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    @Override public LocalDateTime convert(String value, String[] parameters) {
        if (parameters == null || parameters.length == 0 || parameters.length == 1 && "".equals(parameters[0])) {
            return LocalDateTime.parse(value);
        }
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        for (String parameter : parameters) {
            builder.appendPattern('[' + parameter + ']');
        }
        // @formatter:off
        DateTimeFormatter formatter =
            builder.parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
            .toFormatter();
        // @formatter:on
        return LocalDateTime.parse(value, formatter);
    }
}
