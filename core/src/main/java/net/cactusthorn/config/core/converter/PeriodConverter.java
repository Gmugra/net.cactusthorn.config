package net.cactusthorn.config.core.converter;

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.time.Period;

public class PeriodConverter implements Converter<Period> {

    @Override public Period convert(String value) {
        // If it looks like a string that Period.parse can handle, let's try that.
        if (value.startsWith("P") || value.startsWith("-P") || value.startsWith("+P")) {
            return Period.parse(value);
        }
        // ...otherwise we'll perform our own parsing
        return parsePeriod(value);
    }

    private Period parsePeriod(String input) {
        String[] parts = splitNumericAndChar(input);
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

    static String[] splitNumericAndChar(String input) {
        // ATTN: String.trim() may not trim all UTF-8 whitespace characters properly.
        // The original implementation used its own unicodeTrim() method that I decided
        // not to include until the need
        // arises. For more information, see:
        // https://github.com/typesafehub/config/blob/v1.3.0/config/src/main/java/com/typesafe/config/impl/ConfigImplUtil.java#L118-L164

        int i = input.length() - 1;
        while (i >= 0) {
            char c = input.charAt(i);
            if (!Character.isLetter(c)) {
                break;
            }
            i -= 1;
        }
        return new String[] {input.substring(0, i + 1).trim(), input.substring(i + 1).trim()};
    }
}
