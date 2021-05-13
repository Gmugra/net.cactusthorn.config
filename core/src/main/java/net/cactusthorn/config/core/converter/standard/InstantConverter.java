package net.cactusthorn.config.core.converter.standard;

import java.time.Instant;

import net.cactusthorn.config.core.converter.Converter;

public class InstantConverter implements Converter<Instant> {

    @Override public Instant convert(String value, String[] parameters) {
        return Instant.parse(value);
    }
}
