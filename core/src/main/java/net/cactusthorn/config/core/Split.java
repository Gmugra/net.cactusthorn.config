package net.cactusthorn.config.core;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented @Retention(SOURCE) @Target({ TYPE, METHOD }) public @interface Split {
    String value();
    String DEFAULT_SPLIT = ",";
}
