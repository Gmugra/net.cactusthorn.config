package net.cactusthorn.config.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented @Target(ElementType.TYPE) @Retention(RetentionPolicy.SOURCE) public @interface Config {
    enum LoadType {
       MERGE,
       FIRST
    }
}
