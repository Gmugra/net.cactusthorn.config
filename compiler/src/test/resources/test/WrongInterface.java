package test;

import net.cactusthorn.config.core.Config;
import java.util.NavigableMap;

@Config interface WrongInterfaceArgEmpty {

    NavigableMap<String, String> map();
}
