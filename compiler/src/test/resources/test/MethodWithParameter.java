package test;

import net.cactusthorn.config.core.Config;

@Config interface MethodWithParameter {

    String CONST = "CONST";

    String value(String param);
}
