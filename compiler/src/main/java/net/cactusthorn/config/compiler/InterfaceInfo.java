package net.cactusthorn.config.compiler;

import static net.cactusthorn.config.core.Key.KEY_SEPARATOR;

import javax.lang.model.element.TypeElement;

import net.cactusthorn.config.core.Prefix;

public class InterfaceInfo {

    private final String prefix;

    InterfaceInfo(TypeElement interfaceType) {
        prefix = findPrefix(interfaceType);
    }

    private String findPrefix(TypeElement interfaceType) {
        Prefix[] prefixAnnotations = interfaceType.getAnnotationsByType(Prefix.class);
        if (prefixAnnotations.length != 0) {
            return prefixAnnotations[0].value() + KEY_SEPARATOR;
        }
        return "";
    }

    public String prefix() {
        return prefix;
    }
}
