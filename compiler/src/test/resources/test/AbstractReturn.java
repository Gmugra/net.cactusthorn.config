package test;

import net.cactusthorn.config.core.Config;
import java.util.AbstractList;

@Config interface AbstractReturn {
    String CONST = "CONST";
    AbstractList<String> list();
}
