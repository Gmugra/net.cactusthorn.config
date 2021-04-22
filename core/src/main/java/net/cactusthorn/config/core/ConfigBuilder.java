package net.cactusthorn.config.core;

import static net.cactusthorn.config.core.ApiMessages.*;
import static net.cactusthorn.config.core.ApiMessages.Key.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ConfigBuilder<C> {

    public static final String BUILDER_CLASSNAME_PREFIX = "ConfigBuilder$$";

    private final Map<String, String> properties;

    protected ConfigBuilder(Map<String, String> properties) {
        this.properties = properties;
    }

    public abstract C build();

    protected <T> T get(Function<String, T> convert, String key) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return convert.apply(value);
    }

    protected <T> T getDefault(Function<String, T> convert, String key, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return convert.apply(defaultValue);
        }
        return convert.apply(value);
    }

    protected <T> Optional<T> getOptional(Function<String, T> convert, String key) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(convert.apply(value));
    }

    protected <T> List<T> getList(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return asList(convert, value, splitRegEx);
    }

    protected <T> List<T> getListDefault(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return asList(convert, defaultValue, splitRegEx);
        }
        return asList(convert, value, splitRegEx);
    }

    protected <T> Optional<List<T>> getOptionalList(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(asList(convert, value, splitRegEx));
    }

    protected <T> Set<T> getSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return asSet(convert, value, splitRegEx);
    }

    protected <T> Set<T> getSetDefault(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return asSet(convert, defaultValue, splitRegEx);
        }
        return asSet(convert, value, splitRegEx);
    }

    protected <T> Optional<Set<T>> getOptionalSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(asSet(convert, value, splitRegEx));
    }

    protected <T> SortedSet<T> getSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return new TreeSet<>(asList(convert, value, splitRegEx));
    }

    protected <T> SortedSet<T> getSortedSetDefault(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return new TreeSet<>(asList(convert, defaultValue, splitRegEx));
        }
        return new TreeSet<>(asList(convert, value, splitRegEx));
    }

    protected <T> Optional<SortedSet<T>> getOptionalSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(new TreeSet<>(asList(convert, value, splitRegEx)));
    }

    private <T> List<T> asList(Function<String, T> convert, String value, String splitRegEx) {
        return Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toList());
    }

    private <T> Set<T> asSet(Function<String, T> convert, String value, String splitRegEx) {
        return Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toSet());
    }
}
