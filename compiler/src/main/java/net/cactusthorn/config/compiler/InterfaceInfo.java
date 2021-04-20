package net.cactusthorn.config.compiler;

import static net.cactusthorn.config.core.Key.KEY_SEPARATOR;
import static net.cactusthorn.config.core.Split.DEFAULT_SPLIT;

import javax.lang.model.element.TypeElement;

public final class InterfaceInfo {

    private final String prefix;
    private final String split;

    InterfaceInfo(TypeElement interfaceType) {
        Annotations a = new Annotations(interfaceType);
        prefix = a.prefix().map(s -> s + KEY_SEPARATOR).orElse("");
        split = a.split().orElse(DEFAULT_SPLIT);
    }

    public String prefix() {
        return prefix;
    }

    public String split() {
        return split;
    }
}
