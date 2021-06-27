package net.cactusthorn.config.core;

import java.util.HashMap;
import java.util.Map;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.util.ConfigInitializer;

public final class ConfigInitializer_DisabledAutoReload extends ConfigInitializer {
  private static final String[] URIS = new String[] {""};

  ConfigInitializer_DisabledAutoReload(final Loaders loaders) {
    super(loaders);
  }

  @Override
  public Map<String, Object> initialize() {
    ConfigHolder ch = loaders().load(Config_DisabledAutoReload.class.getClassLoader(), LoadStrategy.UNKNOWN, URIS);
    Map<String,Object> values = new HashMap<>();
    values.put("aaa", ch.get(s -> s, "aaa"));
    return values;
  }
}
