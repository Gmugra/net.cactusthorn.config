package net.cactusthorn.config.compiler.methodvalidator;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_VOID;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public class ReturnVoidValidator extends MethodValidatorAncestor {

    public ReturnVoidValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() == TypeKind.VOID) {
            throw new ProcessorException(msg(RETURN_VOID), methodElement);
        }
        return next(methodElement, typeMirror);
    }

}
