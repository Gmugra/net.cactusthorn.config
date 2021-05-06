package net.cactusthorn.config.core;

import java.util.Map;
import java.util.Set;

public interface Accessible {
    Set<String> keys();

    Object get(String key);

    Map<String, Object> asMap();
}
