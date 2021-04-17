package net.cactusthorn.config.tests;

import net.cactusthorn.config.core.Config;

import java.util.*;

@Config interface AllCorrect {

    enum FromStringEnum {
        AAA, BBB;

        public static FromStringEnum fromString(String string) {
            return AAA;
        }
    }

    enum SimpleEnum {
        AAA, BBB;
    }

    FromStringEnum fromStringEnum();

    SimpleEnum simpleEnum();

    String value();

    int intValue();

    Optional<UUID> uuid();

    Optional<List<Integer>> list();

    Set<UUID> set();

    SortedSet<Float> sorted();

    StringBuilder buf();

    Double ddd();
}
