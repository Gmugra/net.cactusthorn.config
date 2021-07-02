package net.cactusthorn.config.core.converter.standard;

import java.time.YearMonth;

public class YearMonthConverter extends DateTimeConverter<YearMonth> {

    @Override public YearMonth convert(String value, String[] parameters) {
        if (isDefaultFormat(parameters)) {
            return YearMonth.parse(value);
        }
        return YearMonth.parse(value, createFormatter(parameters));
    }

}
