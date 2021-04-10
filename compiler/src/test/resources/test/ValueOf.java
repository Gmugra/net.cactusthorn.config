package test;

import net.cactusthorn.config.core.Config;
import java.util.UUID;
import java.util.Set;

@Config public interface ValueOf {

    Integer value();

    UUID uuid();

    StringBuilder buf();

    int simple();

    Set<UUID> set();
}
