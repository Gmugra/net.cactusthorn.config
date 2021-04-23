package net.cactusthorn.config.core.loader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public enum LoadStrategy {
    // @formatter:off
    FIRST(l -> l.stream().filter(m -> !m.isEmpty()).findFirst().orElse(Collections.emptyMap())),
    MERGE(l -> {
        Map<String, String> result = new HashMap<>();
        for (int i = l.size() - 1; i >= 0; i--) {
            result.putAll(l.get(i));
        }
        return result;
    });
    // @formatter:on

    private Function<List<Map<String, String>>, Map<String, String>> strategy;

    LoadStrategy(Function<List<Map<String, String>>, Map<String, String>> strategy) {
        this.strategy = strategy;
    }

    public Map<String, String> combine(List<Map<String, String>> properties) {
        return strategy.apply(properties);
    }
}
