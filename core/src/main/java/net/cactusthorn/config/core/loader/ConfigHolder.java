/*
* Copyright (C) 2021, Alexei Khatskevich
*
* Licensed under the BSD 3-Clause license.
* You may obtain a copy of the License at
*
* https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package net.cactusthorn.config.core.loader;

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.VALUE_NOT_FOUND;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
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
        return (List<T>) asCollection(convert, ArrayList::new, value, splitRegEx);
    }

    public <T> List<T> getList(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return (List<T>) asCollection(convert, ArrayList::new, defaultValue, splitRegEx);
        }
        return (List<T>) asCollection(convert, ArrayList::new, value, splitRegEx);
    }

    public <T> Optional<List<T>> getOptionalList(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of((List<T>) asCollection(convert, ArrayList::new, value, splitRegEx));
    }

    public <T> Set<T> getSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return (Set<T>) asCollection(convert, HashSet::new, value, splitRegEx);
    }

    public <T> Set<T> getSet(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return (Set<T>) asCollection(convert, HashSet::new, defaultValue, splitRegEx);
        }
        return (Set<T>) asCollection(convert, HashSet::new, value, splitRegEx);
    }

    public <T> Optional<Set<T>> getOptionalSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of((Set<T>) asCollection(convert, HashSet::new, value, splitRegEx));
    }

    public <T> SortedSet<T> getSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return (SortedSet<T>) asCollection(convert, TreeSet::new, value, splitRegEx);
    }

    public <T> SortedSet<T> getSortedSet(Function<String, T> convert, String key, String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return (SortedSet<T>) asCollection(convert, TreeSet::new, defaultValue, splitRegEx);
        }
        return (SortedSet<T>) asCollection(convert, TreeSet::new, value, splitRegEx);
    }

    public <T> Optional<SortedSet<T>> getOptionalSortedSet(Function<String, T> convert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of((SortedSet<T>) asCollection(convert, TreeSet::new, value, splitRegEx));
    }

    public <K, V> Map<K, V> getMap(Function<String, K> keyConvert, Function<String, V> valueConvert, String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return asMap(keyConvert, valueConvert, HashMap::new, value, splitRegEx);
    }

    public <K, V> Map<K, V> getMap(Function<String, K> keyConvert, Function<String, V> valueConvert, String key, String splitRegEx,
            String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return asMap(keyConvert, valueConvert, HashMap::new, defaultValue, splitRegEx);
        }
        return asMap(keyConvert, valueConvert, HashMap::new, value, splitRegEx);
    }

    public <K, V> Optional<Map<K, V>> getOptionalMap(Function<String, K> keyConvert, Function<String, V> valueConvert, String key,
            String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(asMap(keyConvert, valueConvert, HashMap::new, value, splitRegEx));
    }

    public <K, V> SortedMap<K, V> getSortedMap(Function<String, K> keyConvert, Function<String, V> valueConvert, String key,
            String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException(msg(VALUE_NOT_FOUND, key));
        }
        return (SortedMap<K, V>) asMap(keyConvert, valueConvert, TreeMap::new, value, splitRegEx);
    }

    public <K, V> SortedMap<K, V> getSortedMap(Function<String, K> keyConvert, Function<String, V> valueConvert, String key,
            String splitRegEx, String defaultValue) {
        String value = properties.get(key);
        if (value == null) {
            return (SortedMap<K, V>) asMap(keyConvert, valueConvert, TreeMap::new, defaultValue, splitRegEx);
        }
        return (SortedMap<K, V>) asMap(keyConvert, valueConvert, TreeMap::new, value, splitRegEx);
    }

    public <K, V> Optional<SortedMap<K, V>> getOptionalSortedMap(Function<String, K> keyConvert, Function<String, V> valueConvert,
            String key, String splitRegEx) {
        String value = properties.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of((SortedMap<K, V>) asMap(keyConvert, valueConvert, TreeMap::new, value, splitRegEx));
    }

    private <T, C extends Collection<T>> Collection<T> asCollection(Function<String, T> convert, Supplier<C> collectionFactory,
            String value, String splitRegEx) {
        return Stream.of(value.split(splitRegEx)).map(convert::apply).collect(Collectors.toCollection(collectionFactory));
    }

    private static final Pattern MAP_SPLIT = Pattern.compile("\\|");

    private <K, V> Map<K, V> asMap(Function<String, K> keyConvert, Function<String, V> valueConvert,
            Supplier<? extends Map<K, V>> mapFactory, String value, String splitRegEx) {
        // @formatter:off
        return
            Stream.of(value.split(splitRegEx))
            .map(s -> MAP_SPLIT.split(s, 2))
            .collect(
                Collectors.toMap(
                    p -> keyConvert.apply(p[0]),
                    p -> valueConvert.apply(p[1]),
                    (u, v) -> {
                        throw new IllegalStateException(String.format("Duplicate key %s", u));
                    },
                    mapFactory));
        // @formatter:on
    }
}
