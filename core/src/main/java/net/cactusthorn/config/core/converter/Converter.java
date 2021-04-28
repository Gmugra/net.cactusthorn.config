package net.cactusthorn.config.core.converter;

public interface Converter<T> {
    T convert(String value);
}
