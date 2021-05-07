package net.cactusthorn.config.core.converter;

public class ToTestConverter implements Converter<String> {

    @Override public String convert(String value) {
        return "test::" + value;
    }

}
