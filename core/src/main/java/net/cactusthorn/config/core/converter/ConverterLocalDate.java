package net.cactusthorn.config.core.converter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.cactusthorn.config.core.converter.standard.LocalDateConverter;

@Retention(SOURCE) @Target(METHOD) @ConverterClass(LocalDateConverter.class) public @interface ConverterLocalDate {
    String[] value() default "";
}
