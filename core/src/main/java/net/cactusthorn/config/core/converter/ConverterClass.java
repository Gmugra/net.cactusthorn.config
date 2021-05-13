package net.cactusthorn.config.core.converter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) @Target({METHOD, ANNOTATION_TYPE}) public @interface ConverterClass {
    Class<? extends Converter<?>> value();
}
