package net.cactusthorn.config.core.configoverride;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.cactusthorn.config.core.loader.Loaders;

public final class Config_ConfigOverride implements ConfigOverride {
  private final ConcurrentHashMap<String, Object> VALUES = new ConcurrentHashMap<>();

  public Config_ConfigOverride(final Loaders loaders) {
    ConfigInitializer_ConfigOverride initializer = new ConfigInitializer_ConfigOverride(loaders);
    VALUES.putAll(initializer.initialize());
  }

  @Override
  public String string() {
    return (String)VALUES.get("test.string");
  }

  @Override
  public int hashCode() {
    return Objects.hash(string());
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append('[');
    buf.append("string").append('=').append(String.valueOf(VALUES.get("test.string")));
    buf.append(']');
    return buf.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Config_ConfigOverride)) return false;
    Config_ConfigOverride other = (Config_ConfigOverride) o;
    return this.string().equals(other.string());
  }
}
