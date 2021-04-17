package net.cactusthorn.config.compiler.methodvalidator;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.METHOD_WITHOUT_PARAMETERS;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public class WithoutParametersValidator extends MethodValidatorAncestor {

    public WithoutParametersValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (!methodElement.getParameters().isEmpty()) {
            throw new ProcessorException(msg(METHOD_WITHOUT_PARAMETERS), methodElement);
        }
        return next(methodElement, typeMirror);
    }
}
