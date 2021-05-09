package net.cactusthorn.config.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.cactusthorn.config.core.loader.LoadStrategy;

@Documented @Target(ElementType.TYPE) @Retention(RetentionPolicy.SOURCE) public @interface Config {
    String[] sources() default "";
    LoadStrategy loadStrategy() default LoadStrategy.UNKNOWN;
}
