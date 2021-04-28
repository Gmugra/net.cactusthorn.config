package net.cactusthorn.config.compiler.methodvalidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;
import net.cactusthorn.config.core.converter.ConverterClass;

public class ConverterValidator extends MethodValidatorAncestor {

    public ConverterValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        try {
            ConverterClass annotation = methodElement.getAnnotation(ConverterClass.class);
            if (annotation != null) {
                annotation.value(); //this will throw MirroredTypeException
            }
            return next(methodElement, typeMirror);
        } catch (MirroredTypeException mte) {
            return new MethodInfo(methodElement).withConverter(mte.getTypeMirror());
        }
    }
}
