package net.cactusthorn.config.tests.key;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Key;
import net.cactusthorn.config.core.Prefix;

@Config @Prefix("prefix") interface WithPrefix {

    String simple();

    @Key("abc") String withKey();
}
