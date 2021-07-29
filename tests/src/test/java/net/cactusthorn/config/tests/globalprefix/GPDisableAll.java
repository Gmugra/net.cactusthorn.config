package net.cactusthorn.config.tests.globalprefix;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Disable;

@Disable(Disable.Feature.GLOBAL_PREFIX)
@Config
public interface GPDisableAll {

    String value();

    String gpValue();
}
