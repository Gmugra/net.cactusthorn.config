package net.cactusthorn.config.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.loader.ReloadEvent;
import net.cactusthorn.config.core.loader.ReloadListener;
import net.cactusthorn.config.core.util.ConfigInitializer;

public final class Config_DisabledAutoReload implements DisabledAutoReload {
  private static final List<ReloadListener> LISTENERS = new ArrayList<>();

  private final ConcurrentHashMap<String, Object> VALUES = new ConcurrentHashMap<>();

  private final ConfigInitializer INITIALIZER;

  public Config_DisabledAutoReload(final Loaders loaders) {
    INITIALIZER = new ConfigInitializer_DisabledAutoReload(loaders);
    VALUES.putAll(INITIALIZER.initialize());
  }

  @Override
  public String aaa() {
    return (String)VALUES.get("aaa");
  }

  @Override
  public int hashCode() {
    return Objects.hash(aaa());
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append('[');
    buf.append("aaa").append('=').append(String.valueOf(VALUES.get("aaa")));
    buf.append(']');
    return buf.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Config_DisabledAutoReload)) return false;
    Config_DisabledAutoReload other = (Config_DisabledAutoReload) o;
    return this.aaa().equals(other.aaa());
  }

  @Override
  public void addReloadListener(ReloadListener listener) {
    LISTENERS.add(listener);
  }

  @Override
  public void reload() {
    Map<String, Object> old = new HashMap<>(VALUES);
    Map<String, Object> reloaded = INITIALIZER.initialize();
    VALUES.entrySet().removeIf(e -> !reloaded.containsKey(e.getKey()));
    VALUES.putAll(reloaded);
    ReloadEvent event = new ReloadEvent(this, old, VALUES);
    LISTENERS.forEach(l -> l.reloadPerformed(event));
  }

  @Override
  public boolean autoReloadable() {
    return false;
  }
}
