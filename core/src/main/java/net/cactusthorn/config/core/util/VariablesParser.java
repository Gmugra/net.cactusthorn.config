package net.cactusthorn.config.core.util;

import static java.text.CharacterIterator.DONE;
import static net.cactusthorn.config.core.ApiMessages.*;
import static net.cactusthorn.config.core.ApiMessages.Key.WRONG_SOURCE_PARAM;

import java.text.StringCharacterIterator;
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
        StringBuilder buf = new StringBuilder();
        StringCharacterIterator it = new StringCharacterIterator(source);
        for (char c = it.first(); c != DONE; c = it.next()) {
            if (c == '{') {
                String variable = processVariable(it);
                buf.append(values.getOrDefault(variable, ""));
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    private String processVariable(StringCharacterIterator it) {

        StringBuilder variable = new StringBuilder();

        char c = it.next();
        do {
            if (c == '{') {
                throw new IllegalArgumentException(msg(WRONG_SOURCE_PARAM));
            }
            if (c == '}') {
                return variable.toString().trim();
            }
            variable.append(c);
            c = it.next();
        } while (c != DONE);

        throw new IllegalArgumentException(msg(WRONG_SOURCE_PARAM));
    }
}
