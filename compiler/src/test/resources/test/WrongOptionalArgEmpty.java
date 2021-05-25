package test;

import net.cactusthorn.config.core.Config;
import java.util.Optional;

@Config interface WrongOptionalArgEmpty {

    @SuppressWarnings("rawtypes") Optional map();
}
