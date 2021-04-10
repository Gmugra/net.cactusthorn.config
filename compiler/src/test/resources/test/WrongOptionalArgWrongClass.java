package test;

import net.cactusthorn.config.core.Config;
import java.util.Optional;
import java.util.ArrayList;

@Config interface WrongOptionalArgWrongClass {

    Optional<ArrayList> map();
}
