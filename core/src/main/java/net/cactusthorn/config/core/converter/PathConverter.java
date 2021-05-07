package net.cactusthorn.config.core.converter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathConverter implements Converter<Path> {

    @Override public Path convert(String value) {
        return Paths.get(value);
    }
}
