package net.cactusthorn.config.compiler;

import static net.cactusthorn.config.core.Key.KEY_SEPARATOR;
import static net.cactusthorn.config.core.Split.DEFAULT_SPLIT;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.core.Accessible;

public final class InterfaceInfo {

    private final String prefix;
    private final String split;
    private final Optional<Long> serialVersionUID;
    private final boolean accessible;

    InterfaceInfo(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement) {
        Annotations a = new Annotations(interfaceTypeElement);
        prefix = a.prefix().map(s -> s + KEY_SEPARATOR).orElse("");
        split = a.split().orElse(DEFAULT_SPLIT);
        serialVersionUID = findSerializable(processingEnv, interfaceTypeElement);
        accessible = findAccessible(processingEnv, interfaceTypeElement);
    }

    public String prefix() {
        return prefix;
    }

    public String split() {
        return split;
    }

    public Optional<Long> serialVersionUID() {
        return serialVersionUID;
    }

    public boolean accessible() {
        return accessible;
    }

    private boolean findAccessible(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement) {
        TypeMirror accessibleType = processingEnv.getElementUtils().getTypeElement(Accessible.class.getName()).asType();
        return processingEnv.getTypeUtils().isAssignable(interfaceTypeElement.asType(), accessibleType);
    }

    private Optional<Long> findSerializable(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement) {
        TypeMirror serializableType = processingEnv.getElementUtils().getTypeElement(Serializable.class.getName()).asType();
        if (processingEnv.getTypeUtils().isAssignable(interfaceTypeElement.asType(), serializableType)) {
            // @formatter:off
            return
                Optional.of(
                    interfaceTypeElement.getEnclosedElements()
                        .stream()
                        .filter(e -> e.getKind() == ElementKind.FIELD)
                        .map(VariableElement.class::cast)
                        .filter(ve -> ve.asType().getKind() == TypeKind.LONG)
                        .filter(ve -> ve.getSimpleName().toString().equals("serialVersionUID"))
                        .map(VariableElement::getConstantValue)
                        .map(Long.class::cast)
                        .findAny()
                            .orElse(0L));
            // @formatter:on
        }
        return Optional.empty();
    }
}
