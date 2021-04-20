package net.cactusthorn.config.tests.defaultvalue;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Default;
import net.cactusthorn.config.core.Split;

@Config interface DefaultValues {

    @Default("string") String str();

    @Default("A,B,C") List<String> list();

    @Default("A:B:B") @Split(":") Set<String> set();

    @Default("B:B:C:C:B") @Split(":") SortedSet<String> sorted();
}
