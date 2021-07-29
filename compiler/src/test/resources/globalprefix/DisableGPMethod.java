package globalprefix;

import net.cactusthorn.config.core.Disable;
import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Prefix;

@Config @Prefix("aaa") public interface DisableGPMethod {

    @Disable({Disable.Feature.GLOBAL_PREFIX, Disable.Feature.PREFIX})
    String value();

    String gpValue();
}
