package net.cactusthorn.config.core.configoverride;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.util.ConfigInitializer;

public final class ConfigInitializer_ConfigOverride extends ConfigInitializer {
  private static final String[] URIS = new String[] {"classpath:config/testconfig2.properties", "classpath:config/testconfig.properties"};

  ConfigInitializer_ConfigOverride(final Loaders loaders) {
    super(loaders);
  }

  @Override
  public Map<String, Object> initialize() {
    ConfigHolder ch = loaders().load(Config_ConfigOverride.class.getClassLoader(), LoadStrategy.FIRST, URIS);
    Map<String,Object> values = new HashMap<>();
    values.put("test.string", ch.get(s -> s, "test.string"));
    return values;
  }
}
