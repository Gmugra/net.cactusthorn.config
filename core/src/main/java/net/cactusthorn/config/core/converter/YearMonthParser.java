package net.cactusthorn.config.core.converter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.cactusthorn.config.core.converter.standard.YearMonthConverter;

@Retention(SOURCE) @Target(METHOD) @ConverterClass(YearMonthConverter.class) public @interface YearMonthParser {
    String[] value() default "";
}
