package net.cactusthorn.config.core.converter;

import java.time.Instant;

public class InstantConverter implements Converter<Instant> {

    @Override public Instant convert(String value) {
        return Instant.parse(value);
    }
}
