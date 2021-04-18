package net.cactusthorn.config.tests.key;

import static net.cactusthorn.config.core.Disable.Feature.*;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Disable;
import net.cactusthorn.config.core.Key;
import net.cactusthorn.config.core.Prefix;

@Config @Prefix("prefix") interface WithPrefix {

    String simple();

    @Key("abc") String withKey();

    @Key("xyz") @Disable(PREFIX) String disabled();
}
