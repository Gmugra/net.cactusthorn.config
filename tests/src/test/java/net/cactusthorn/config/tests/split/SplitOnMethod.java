package net.cactusthorn.config.tests.split;

import java.util.List;
import java.util.Optional;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Split;

@Config interface SplitOnMethod {

    List<String> list();

    @Split("[:;]") Optional<List<String>> list2();
}
