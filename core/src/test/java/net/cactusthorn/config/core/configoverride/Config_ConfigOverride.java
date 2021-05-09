package net.cactusthorn.config.core.configoverride;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class Config_ConfigOverride implements ConfigOverride {
  private final ConcurrentHashMap<String, Object> VALUES = new ConcurrentHashMap<>();

  Config_ConfigOverride(final Map<String, Object> values) {
    VALUES.putAll(values);
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
    if (!this.string().equals(other.string())) return false;
    return true;
  }
}

