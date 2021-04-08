package test;

import net.cactusthorn.config.core.Config;

import java.util.Optional;
import java.util.UUID;

@Config interface Simple {
    String value();

    int intValue();

    Optional<UUID> uuid();
}
