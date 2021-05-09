package net.cactusthorn.config.core.loader;

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.VALUE_NOT_FOUND;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ConfigHolder {

    private final Map<String, String> properties;

    ConfigHolder(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    public String getString(String key) {
        return get(Function.identity(), key);
    }

    public String getString(String key, String defaultValue) {
        return get(Function.identity(), key, defaultValue);
    }

    public Optional<String> getOptionalString(String key) {
        return getOptional(Function.identity(), key);
    }

    public char getChar(String key) {
        return get(s -> s.charAt(0), key);
    }

    public char getChar(String key, char defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.charAt(0);
    }

    public Optional<Character> getOptionalChar(String key) {
        return getOptional(s -> s.charAt(0), key);
    }

    public int getInt(String key) {
        return get(Integer::valueOf, key);
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Integer.valueOf(value);
    }

    public Optional<Integer> getOptionalInt(String key) {
        return getOptional(Integer::valueOf, key);
    }

    public byte getByte(String key) {
        return get(Byte::valueOf, key);
    }

    public byte getByte(String key, byte defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Byte.valueOf(value);
    }

    public Optional<Byte> getOptionalByte(String key) {
        return getOptional(Byte::valueOf, key);
    }

    public short getShort(String key) {
        return get(Short::valueOf, key);
    }

    public short getShort(String key, short defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Short.valueOf(value);
    }

    public Optional<Short> getOptionalShort(String key) {
        return getOptional(Short::valueOf, key);
    }

    public long getLong(String key) {
        return get(Long::valueOf, key);
    }

    public long getLong(String key, long defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Long.valueOf(value);
    }

    public Optional<Long> getOptionalLong(String key) {
        return getOptional(Long::valueOf, key);
    }

    public float getFloat(String key) {
        return get(Float::valueOf, key);
    }

    public float getFloat(String key, float defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Float.valueOf(value);
    }

    public Optional<Float> getOptionalFloat(String key) {
        return getOptional(Float::valueOf, key);
    }

    public double getDouble(String key) {
        return get(Double::valueOf, key);
    }

    public double getDouble(String key, double defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Double.valueOf(value);
    }

    public Optional<Double> getOptionalDouble(String key) {
        return getOptional(Double::valueOf, key);
    }

    public boolean getBoolean(String key) {
        return get(Boolean::valueOf, key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.valueOf(value);
    }

    public Optional<Boolean> getOptionalBoolean(String key) {
        return getOptional(Boolean::valueOf, key);
    }

    public <T> T get(Function<String, T> convert, String key) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return convert.apply(value);
    }

    public <T> T get(Function<String, T> convert, String key, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return convert.apply(defaultValue);
        }
        return convert.apply(value);
    }

    public <T> Optional<T> getOptional(Function<String, T> convert, String key) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(convert.apply(value));
    }

    public <T> List<T> getList(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return asList(convert, value, splitRegEx);
    }

    public <T> List<T> getList(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return asList(convert, defaultValue, splitRegEx);
        }
        return asList(convert, value, splitRegEx);
    }

    public <T> Optional<List<T>> getOptionalList(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(asList(convert, value, splitRegEx));
    }

    public <T> Set<T> getSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return asSet(convert, value, splitRegEx);
    }

    public <T> Set<T> getSet(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return asSet(convert, defaultValue, splitRegEx);
        }
        return asSet(convert, value, splitRegEx);
    }

    public <T> Optional<Set<T>> getOptionalSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(asSet(convert, value, splitRegEx));
    }

    public <T> SortedSet<T> getSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return new TreeSet<>(asList(convert, value, splitRegEx));
    }

    public <T> SortedSet<T> getSortedSet(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return new TreeSet<>(asList(convert, defaultValue, splitRegEx));
        }
        return new TreeSet<>(asList(convert, value, splitRegEx));
    }

    public <T> Optional<SortedSet<T>> getOptionalSortedSet(Function<String, T> convert, String key, String splitRegEx) {
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
