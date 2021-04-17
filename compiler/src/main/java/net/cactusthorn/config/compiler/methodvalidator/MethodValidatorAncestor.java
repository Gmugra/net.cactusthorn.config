package net.cactusthorn.config.compiler.methodvalidator;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public abstract class MethodValidatorAncestor implements MethodValidator {

    private final ProcessingEnvironment processingEnv;

    public MethodValidatorAncestor(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected ProcessingEnvironment processingEnv() {
        return processingEnv;
    }

    private static final MethodType CONSTRUCTOR = MethodType.methodType(void.class, ProcessingEnvironment.class);

    static MethodValidatorAncestor create(ProcessingEnvironment pe, Class<? extends MethodValidatorAncestor> clazz) {
        try {
            return (MethodValidatorAncestor) MethodHandles.publicLookup().findConstructor(clazz, CONSTRUCTOR).invoke(pe);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private MethodValidator next;

    void setNext(MethodValidator validator) {
        next = validator;
    }

    protected MethodInfo next(ExecutableElement methodElement, TypeMirror typeMirror) {
        if (next != null) {
            return next.validate(methodElement, typeMirror);
        }
        return new MethodInfo(methodElement);
    }
}
