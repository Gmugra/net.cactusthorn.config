package net.cactusthorn.config.compiler.methodvalidator;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public interface MethodValidator {
    MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException;
}
