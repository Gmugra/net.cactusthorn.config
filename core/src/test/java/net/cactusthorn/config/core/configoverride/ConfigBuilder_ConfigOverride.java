package net.cactusthorn.config.core.configoverride;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import net.cactusthorn.config.core.ConfigBuilder;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loaders;

public final class ConfigBuilder_ConfigOverride extends ConfigBuilder<Config_ConfigOverride> {
  private static final String[] URIS = new String[] {"classpath:config/testconfig2.properties", "classpath:config/testconfig.properties"};

  public ConfigBuilder_ConfigOverride(final Loaders loaders) {
    super(loaders);
  }

  @Override
  public Config_ConfigOverride build() {
    ConfigHolder ch = loaders().load(Config_ConfigOverride.class.getClassLoader(), LoadStrategy.FIRST, URIS);
    Map<String,Object> values = new HashMap<>();
    values.put("test.string", ch.get(s -> s, "test.string"));
    return new Config_ConfigOverride(values);
  }
}

