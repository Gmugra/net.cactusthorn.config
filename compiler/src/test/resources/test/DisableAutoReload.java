package test;

import net.cactusthorn.config.core.Reloadable;
import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Disable;

@Config
@Disable(Disable.Feature.AUTO_RELOAD)
public interface DisableAutoReload extends Reloadable {

    String value();
}
