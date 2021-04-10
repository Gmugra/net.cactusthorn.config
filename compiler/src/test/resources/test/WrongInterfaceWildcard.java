package test;

import net.cactusthorn.config.core.Config;
import java.util.List;

@Config interface WrongInterfaceWildcard {

    List<?> map();
}
