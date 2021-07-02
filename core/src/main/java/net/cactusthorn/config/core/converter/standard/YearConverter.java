package net.cactusthorn.config.core.converter.standard;

import java.time.Year;

public class YearConverter extends DateTimeConverter<Year> {

    @Override public Year convert(String value, String[] parameters) {
        if (isDefaultFormat(parameters)) {
            return Year.parse(value);
        }
        return Year.parse(value, createFormatter(parameters));
    }
}
