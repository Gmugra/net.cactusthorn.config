package test;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Default;

import java.util.Optional;

@Config interface WrongOptionalDefaultAnnotation {

    @Default("wrong") Optional<String> val();
}
