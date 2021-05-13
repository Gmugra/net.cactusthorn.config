package net.cactusthorn.config.core.converter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.cactusthorn.config.core.converter.standard.ZonedDateTimeConverter;

@Retention(SOURCE) @Target(METHOD) @ConverterClass(ZonedDateTimeConverter.class) public @interface ConverterZonedDateTime {
    String[] value() default "";
}
