package net.cactusthorn.config.core.converter.standard;

import java.net.URL;

import net.cactusthorn.config.core.converter.Converter;

import java.net.MalformedURLException;

public class URLConverter implements Converter<URL> {

    @Override public URL convert(String value, String[] parameters) {
        try {
            return new URL(value);
        } catch (MalformedURLException mue) {
            throw new IllegalArgumentException(mue.getMessage(), mue);
        }
    }
}
