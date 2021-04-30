package net.cactusthorn.config.core.converter;

import java.net.URL;
import java.net.MalformedURLException;

public class URLConverter implements Converter<URL> {

    @Override public URL convert(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException mue) {
            throw new IllegalArgumentException(mue.getMessage(), mue);
        }
    }
}
