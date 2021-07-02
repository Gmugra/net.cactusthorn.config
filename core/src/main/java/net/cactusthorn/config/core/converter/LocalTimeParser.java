package net.cactusthorn.config.core.converter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.cactusthorn.config.core.converter.standard.LocalTimeConverter;

@Retention(SOURCE) @Target(METHOD) @ConverterClass(LocalTimeConverter.class) public @interface LocalTimeParser {
    String[] value() default "";
}
