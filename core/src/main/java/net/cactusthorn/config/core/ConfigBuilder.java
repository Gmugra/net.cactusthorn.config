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

    /*
    protected <T> T get(Function<String, T> convert, String key) {
        return configHolder.get(convert, key);
    }

    protected <T> T get(Function<String, T> convert, String key, String defaultValue) {
        return configHolder.get(convert, key, defaultValue);
    }

    protected <T> Optional<T> getOptional(Function<String, T> convert, String key) {
        return configHolder.getOptional(convert, key);
    }

    protected <T> List<T> getList(Function<String, T> convert, String key, String splitRegEx) {
        return configHolder.getList(convert, key, splitRegEx);
    }

    protected <T> List<T> getList(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        return configHolder.getList(convert, key, splitRegEx, defaultValue);
    }

    protected <T> Optional<List<T>> getOptionalList(Function<String, T> convert, String key, String splitRegEx) {
        return configHolder.getOptionalList(convert, key, splitRegEx);
    }

    protected <T> Set<T> getSet(Function<String, T> convert, String key, String splitRegEx) {
        return configHolder.getSet(convert, key, splitRegEx);
    }

    protected <T> Set<T> getSet(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        return configHolder.getSet(convert, key, splitRegEx, defaultValue);
    }

    protected <T> Optional<Set<T>> getOptionalSet(Function<String, T> convert, String key, String splitRegEx) {
        return configHolder.getOptionalSet(convert, key, splitRegEx);
    }

    protected <T> SortedSet<T> getSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        return configHolder.getSortedSet(convert, key, splitRegEx);
    }

    protected <T> SortedSet<T> getSortedSet(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        return configHolder.getSortedSet(convert, key, splitRegEx, defaultValue);
    }

    protected <T> Optional<SortedSet<T>> getOptionalSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        return configHolder.getOptionalSortedSet(convert, key, splitRegEx);
    }
    */
}
