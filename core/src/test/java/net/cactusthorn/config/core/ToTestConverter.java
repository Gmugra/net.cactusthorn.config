package net.cactusthorn.config.core;

import net.cactusthorn.config.core.converter.Converter;

public class ToTestConverter implements Converter<String> {

    @Override public String convert(String value, String[] parameters) {
        return "test::" + value;
    }

}
