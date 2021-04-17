package net.cactusthorn.config.compiler.methodvalidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public final class MethodValidatorChain implements MethodValidator {

    private final MethodValidator validator;

    private MethodValidatorChain(MethodValidator validator) {
        this.validator = validator;
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        return validator.validate(methodElement, typeMirror);
    }

    public static Builder builder(ProcessingEnvironment processingEnv, Class<? extends MethodValidatorAncestor> clazz) {
        return new Builder(processingEnv, clazz);
    }

    public static final class Builder {

        private final ProcessingEnvironment processingEnv;

        private MethodValidatorAncestor first;
        private MethodValidatorAncestor last;

        private Builder(ProcessingEnvironment processingEnv, Class<? extends MethodValidatorAncestor> clazz) {
            this.processingEnv = processingEnv;
            first = MethodValidatorAncestor.create(processingEnv, clazz);
            last = first;
        }

        public Builder next(Class<? extends MethodValidatorAncestor> clazz) {
            MethodValidatorAncestor validator = MethodValidatorAncestor.create(processingEnv, clazz);
            last.setNext(validator);
            last = validator;
            return this;
        }

        public MethodValidator build() {
            return new MethodValidatorChain(first);
        }
    }
}
