package net.cactusthorn.config.core.converter.standard;

import java.time.LocalTime;

public class LocalTimeConverter extends DateTimeConverter<LocalTime> {

    @Override public LocalTime convert(String value, String[] parameters) {
        if (isDefaultFormat(parameters)) {
            return LocalTime.parse(value);
        }
        return LocalTime.parse(value, createFormatter(parameters));
    }
}
