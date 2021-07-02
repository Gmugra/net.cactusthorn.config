package net.cactusthorn.config.core.converter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.cactusthorn.config.core.converter.standard.YearConverter;

@Retention(SOURCE) @Target(METHOD) @ConverterClass(YearConverter.class) public @interface YearParser {
    String[] value() default "";
}
