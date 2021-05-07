package net.cactusthorn.config.core.util;

import static net.cactusthorn.config.core.ApiMessages.*;
import static net.cactusthorn.config.core.ApiMessages.Key.WRONG_SOURCE_PARAM;

import java.util.Map;

public final class VariablesParser {

    private final String source;

    public VariablesParser(String source) {
        if (source == null) {
            throw new IllegalArgumentException(isNull("source"));
        }
        String prepared = source.trim();
        if (prepared.isEmpty()) {
            throw new IllegalArgumentException(isEmpty("source"));
        }
        this.source = prepared;
    }

    public String replace(final Map<String, String> values) {
        if (values == null) {
            throw new IllegalArgumentException(isNull("values"));
        }

        StringBuilder result = new StringBuilder();

        int pos = 0;
        int start = source.indexOf('{');
        while (start >= 0) {
            result.append(source.substring(pos, start));
            pos = source.indexOf('}', start + 1);
            if (pos == -1) {
                throw new IllegalArgumentException(msg(WRONG_SOURCE_PARAM, start, source));
            }
            String variable = source.substring(start + 1, pos);
            if (variable.indexOf('{') != -1) {
                throw new IllegalArgumentException(msg(WRONG_SOURCE_PARAM, start + 1 + variable.indexOf('{'), source));
            }
            String value = values.getOrDefault(variable, "");
            result.append(value);
            pos++;
            start = source.indexOf('{', pos);
        }
        result.append(source.substring(pos));
        if (result.indexOf("}") != -1) {
            throw new IllegalArgumentException(msg(WRONG_SOURCE_PARAM, source, source.indexOf("}", pos)));
        }
        return result.toString();
    }
}
