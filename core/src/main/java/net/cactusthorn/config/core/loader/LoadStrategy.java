package net.cactusthorn.config.core.loader;

import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public enum LoadStrategy {

    // @formatter:off
    FIRST(l -> first(new HashMap<>(), l)),
    MERGE(l -> merge(new HashMap<>(), l)),
    FIRST_KEYCASEINSENSITIVE(l -> first(new TreeMap<>(String.CASE_INSENSITIVE_ORDER), l)),
    MERGE_KEYCASEINSENSITIVE(l -> merge(new TreeMap<>(String.CASE_INSENSITIVE_ORDER), l));
    // @formatter:on

    private final Function<List<Map<String, String>>, Map<String, String>> strategy;

    LoadStrategy(Function<List<Map<String, String>>, Map<String, String>> strategy) {
        this.strategy = strategy;
    }

    public Map<String, String> combine(List<Map<String, String>> properties, Map<String, String> manualProperties) {
        Map<String, String> result = strategy.apply(properties);
        result.putAll(manualProperties); // Map with properties is always has highest priority
        return Collections.unmodifiableMap(result);
    }

    private static Map<String, String> first(Map<String, String> result, List<Map<String, String>> list) {
        for (Map<String, String> uriProperties : list) {
            if (!uriProperties.isEmpty()) {
                result.putAll(uriProperties);
                break;
            }
        }
        return result;
    }

    private static Map<String, String> merge(Map<String, String> result, List<Map<String, String>> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            result.putAll(list.get(i));
        }
        return result;
    }
}
