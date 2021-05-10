package net.cactusthorn.config.core.converter.standard;

import java.net.URI;

import net.cactusthorn.config.core.converter.Converter;

public class URIConverter implements Converter<URI> {

    @Override public URI convert(String value) {
        return URI.create(value);
    }

}
