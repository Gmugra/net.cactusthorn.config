package net.cactusthorn.config.core.converter;

public interface Converter<T> {

    String[] EMPTY = new String[] {""};

    default T convert(String value) {
        return convert(value, EMPTY);
    }

    T convert(String value, String[] parameters);
}
