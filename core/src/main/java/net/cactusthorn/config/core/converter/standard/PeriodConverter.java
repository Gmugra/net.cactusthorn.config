package net.cactusthorn.config.core.converter.standard;

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.time.Period;

import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.util.NumericAndCharSplitter;

public class PeriodConverter implements Converter<Period> {

    private static final NumericAndCharSplitter SPLITTER = new NumericAndCharSplitter();

    @Override public Period convert(String value, String[] parameters) {
        // If it looks like a string that Period.parse can handle, let's try that.
        if (value.startsWith("P") || value.startsWith("-P") || value.startsWith("+P")) {
            return Period.parse(value);
        }
        // ...otherwise we'll perform our own parsing
        return parsePeriod(value);
    }

    private Period parsePeriod(String input) {
        String[] parts = SPLITTER.split(input);
        String numberString = parts[0];
        String originalUnitString = parts[1];
        String unitString = originalUnitString;

        if (numberString.length() == 0) {
            throw new IllegalArgumentException(msg(PERIOD_NO_NUMBER, input));
        }

        if (unitString.length() > 2 && !unitString.endsWith("s")) {
            unitString = unitString + 's';
        }

        // note that this is deliberately case-sensitive
        switch (unitString) {
        case "":
        case "d":
        case "days":
            return Period.ofDays(Integer.parseInt(numberString));
        case "w":
        case "weeks":
            return Period.ofWeeks(Integer.parseInt(numberString));
        case "m":
        case "mo":
        case "months":
            return Period.ofMonths(Integer.parseInt(numberString));
        case "y":
        case "years":
            return Period.ofYears(Integer.parseInt(numberString));
        default:
            throw new IllegalArgumentException(msg(PERIOD_WRONG_TIME_UNIT, originalUnitString));
        }
    }
}
