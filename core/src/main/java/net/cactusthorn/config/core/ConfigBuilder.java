package net.cactusthorn.config.core;

import java.util.concurrent.ConcurrentHashMap;

import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.loader.Loaders;

public abstract class ConfigBuilder<C> {

    public static final String CONFIG_CLASSNAME_PREFIX = "Config_";
    public static final String BUILDER_CLASSNAME_PREFIX = "ConfigBuilder_";

    protected static final ConcurrentHashMap<Class<?>, Converter<?>> CONVERTERS = new ConcurrentHashMap<>();

    private final Loaders loaders;

    protected ConfigBuilder(Loaders loaders) {
        this.loaders = loaders;
    }

    public abstract C build();

    @SuppressWarnings("unchecked") protected <T> T convert(Class<? extends Converter<T>> clazz, String value) {
        return (T) CONVERTERS.get(clazz).convert(value);
    }

    protected Loaders loaders() {
        return loaders;
    }
}
