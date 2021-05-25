package test;

import net.cactusthorn.config.core.Config;
import java.util.List;

@Config interface WrongInterface {

    @SuppressWarnings("rawtypes") List map();
}
