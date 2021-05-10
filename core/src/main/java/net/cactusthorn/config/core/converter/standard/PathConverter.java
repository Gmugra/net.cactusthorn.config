package net.cactusthorn.config.core.converter.standard;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.cactusthorn.config.core.converter.Converter;

public class PathConverter implements Converter<Path> {

    @Override public Path convert(String value) {
        return Paths.get(value);
    }
}
