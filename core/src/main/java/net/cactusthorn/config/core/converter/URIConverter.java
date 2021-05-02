package net.cactusthorn.config.core.converter;

import java.net.URI;

public class URIConverter implements Converter<URI> {

    @Override public URI convert(String value) {
        return URI.create(value);
    }

}
