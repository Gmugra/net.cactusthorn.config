package test;

import net.cactusthorn.config.core.Config;

import java.util.Optional;
import java.util.UUID;

@Config interface MethodWithParameter {

    String CONST = "CONST";

    public String value(String param);
}
