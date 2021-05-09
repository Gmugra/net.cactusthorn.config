package net.cactusthorn.config.core.configoverride;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Prefix;
import net.cactusthorn.config.core.loader.LoadStrategy;

@Config(sources = {"classpath:config/testconfig2.properties","classpath:config/testconfig.properties"}, loadStrategy = LoadStrategy.FIRST)//
@Prefix("test") // 
public interface ConfigOverride {

    String string();
}
