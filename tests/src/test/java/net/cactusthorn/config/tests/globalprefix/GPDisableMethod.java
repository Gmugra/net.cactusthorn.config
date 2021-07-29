package net.cactusthorn.config.tests.globalprefix;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Disable;
import net.cactusthorn.config.core.Key;

@Config public interface GPDisableMethod {

    @Disable(Disable.Feature.GLOBAL_PREFIX) String value();

    @Key("{xxx}.gpValue")
    String gpValue();
}
