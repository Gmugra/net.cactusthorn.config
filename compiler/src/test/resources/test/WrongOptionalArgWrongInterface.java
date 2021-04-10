package test;

import net.cactusthorn.config.core.Config;
import java.util.Optional;
import java.util.Map;

@Config interface WrongOptionalArgWrongInterface {

    Optional<Map<?,?>> map();
}
