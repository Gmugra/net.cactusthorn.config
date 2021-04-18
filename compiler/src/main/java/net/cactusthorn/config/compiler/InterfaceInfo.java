package net.cactusthorn.config.compiler;

import static net.cactusthorn.config.core.Key.KEY_SEPARATOR;

import javax.lang.model.element.TypeElement;

import net.cactusthorn.config.core.Prefix;
import net.cactusthorn.config.core.Split;

public class InterfaceInfo {

    private final String prefix;
    private final String split;

    InterfaceInfo(TypeElement interfaceType) {
        prefix = findPrefix(interfaceType);
        split = findSplit(interfaceType);
    }

    private String findPrefix(TypeElement interfaceType) {
        Prefix[] prefixAnnotations = interfaceType.getAnnotationsByType(Prefix.class);
        if (prefixAnnotations.length != 0) {
            return prefixAnnotations[0].value() + KEY_SEPARATOR;
        }
        return "";
    }

    private String findSplit(TypeElement interfaceType) {
        Split[] splitAnnotations = interfaceType.getAnnotationsByType(Split.class);
        if (splitAnnotations.length != 0) {
            return splitAnnotations[0].value();
        }
        return Split.DEFAULT_SPLIT;
    }

    public String prefix() {
        return prefix;
    }

    public String split() {
        return split;
    }
}
