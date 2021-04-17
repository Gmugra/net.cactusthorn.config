package net.cactusthorn.config.core;

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

    protected Map<String, String> properties() {
        return properties;
    }

    public abstract C build();

    protected <T> T get(Function<String, T> convert, String key) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Value for key " + key + " is not found."); // TODO message
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
            throw new IllegalArgumentException("Value for key " + key + " is not found."); // TODO message
        }
        return Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toList());
    }

    protected <T> Optional<List<T>> getOptionalList(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toList()));
    }

    protected <T> Set<T> getSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Value for key " + key + "is not found."); // TODO message
        }
        return Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toSet());
    }

    protected <T> Optional<Set<T>> getOptionalSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toSet()));
    }

    protected <T> SortedSet<T> getSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Value for key " + key + "is not found."); // TODO message
        }
        return new TreeSet<>(Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toList()));
    }

    protected <T> Optional<SortedSet<T>> getOptionalSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Value for key " + key + "is not found."); // TODO message
        }
        return Optional.of(new TreeSet<>(Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toList())));
    }
}
