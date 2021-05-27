package test;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Key;

@Config public interface KeyWithSysProperty {

    @Key("{sysproperty}.value")
    String value();
}
