package net.cactusthorn.config.tests.key;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Key;

@Config interface WithKey {

    String simple();

    @Key("abc") String withKey();
}
