package test;

import net.cactusthorn.config.core.Config;

import java.util.*;

@Config interface AllCorrect {
    String value();

    int intValue();

    Optional<UUID> uuid();

    Optional<List<Integer>> list();

    Set<UUID> set();

    SortedSet<Float> sorted();

    StringBuilder buf();
}
