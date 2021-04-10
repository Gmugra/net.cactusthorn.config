package test;

import net.cactusthorn.config.core.Config;
import java.util.AbstractList;

import java.util.Optional;
import java.util.UUID;

@Config interface AbstractReturn {
    String CONST = "CONST";
    AbstractList<String> list();
}
