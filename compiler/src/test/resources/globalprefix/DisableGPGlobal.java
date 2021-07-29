package globalprefix;

import net.cactusthorn.config.core.Disable;
import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Prefix;

@Config @Prefix("aaa") @Disable(Disable.Feature.GLOBAL_PREFIX) public interface DisableGPGlobal {

    @Disable(Disable.Feature.PREFIX)
    String value();

    String gpValue();
}
