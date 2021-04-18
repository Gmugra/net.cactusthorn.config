package net.cactusthorn.config.tests.split;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Split;

@Config @Split("[:;]") interface SplitOnInterface {

    List<String> list();

    @Split(",") Optional<SortedSet<String>> list2();
}
