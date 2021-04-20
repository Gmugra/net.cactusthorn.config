package net.cactusthorn.config.core;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented @Retention(SOURCE) @Target(METHOD) public @interface Default {
    String value();
}
