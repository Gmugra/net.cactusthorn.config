package test;

import net.cactusthorn.config.core.Config;
import java.util.Optional;
import java.util.NavigableMap;

@Config interface WrongOptionalArgWrongInterface {

    Optional<NavigableMap<?, ?>> map();
}
