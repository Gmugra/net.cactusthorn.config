package test;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Prefix;
import net.cactusthorn.config.core.Disable;
import net.cactusthorn.config.core.Split;
import net.cactusthorn.config.core.Key;

import java.util.*;

@Config @Prefix("prefix") @Split(";") interface AllCorrect {

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

    @Split(",") Optional<List<Integer>> list();

    Set<UUID> set();

    SortedSet<Float> sorted();

    StringBuilder buf();

    @Key("ddd") @Disable(Disable.Feature.PREFIX) Double ddd();

    List<String> listNoOptional();

    Optional<SortedSet<Float>> sortedOptional();

    Optional<Set<Float>> setOptional();

    Float boxedPrimitive();
}
