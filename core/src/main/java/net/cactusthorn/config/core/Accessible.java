package net.cactusthorn.config.core;

import java.util.Map;
import java.util.Set;

public interface Accessible {
    Set<String> getKeys();

    Object getProperty(String key);

    Map<String, Object> getProperties();
}
