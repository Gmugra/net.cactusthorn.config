package net.cactusthorn.config.core.converter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.cactusthorn.config.core.converter.standard.LocalDateTimeConverter;

@Retention(SOURCE) @Target(METHOD) @ConverterClass(LocalDateTimeConverter.class) public @interface ConverterLocalDateTime {
    String[] value() default "";
}
