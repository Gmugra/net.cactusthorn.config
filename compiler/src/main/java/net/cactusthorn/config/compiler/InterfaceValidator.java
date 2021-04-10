package net.cactusthorn.config.compiler;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.*;

import java.util.Optional;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;

public class InterfaceValidator {

    InterfaceValidator() {
    }

    void validate(Element element) {
        validateInterface(element);
        validateMethodExist(element);
        validateParameters(element);
    }

    private void validateInterface(Element element) {
        if (element.getKind() != ElementKind.INTERFACE) {
            throw new ProcessorException(msg(ONLY_INTERFACE), element);
        }
    }

    private void validateMethodExist(Element element) {
        if (!((TypeElement) element).getEnclosedElements().stream().filter(e -> e.getKind() == ElementKind.METHOD).findAny().isPresent()) {
            throw new ProcessorException(msg(METHOD_MUST_EXIST), element);
        }
    }

    private void validateParameters(Element element) {
        // @formatter:off
        Optional<? extends Element> method =
                ((TypeElement) element)
                .getEnclosedElements()
                .stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .filter(e -> !((ExecutableType) e.asType()).getParameterTypes().isEmpty())
                .findAny();
        // @formatter:on
        if (method.isPresent()) {
            throw new ProcessorException(msg(METHOD_WITHOUT_PARAMETERS), method.get());
        }
    }
}
