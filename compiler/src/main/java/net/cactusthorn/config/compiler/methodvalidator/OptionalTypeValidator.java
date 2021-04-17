package net.cactusthorn.config.compiler.methodvalidator;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_OPTIONAL_ARG_EMPTY;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_OPTIONAL_ARG_WILDCARD;

import java.util.List;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public class OptionalTypeValidator extends MethodValidatorAncestor {

    private final TypeMirror optionalTM;

    private final MethodValidator argumentValidator;

    public OptionalTypeValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        optionalTM = processingEnv.getElementUtils().getTypeElement(Optional.class.getName()).asType();
        // @formatter:off
        argumentValidator = MethodValidatorChain.builder(processingEnv, InterfaceTypeValidator.class)
            .next(AbstractTypeValidator.class)
            .next(StringTypeValidator.class)
            .build();
        // @formatter:on
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        Element element = declaredType.asElement();
        TypeMirror optionalTypeMirror = element.asType();
        if (!processingEnv().getTypeUtils().isSameType(optionalTM, optionalTypeMirror)) {
            return next(methodElement, typeMirror);
        }
        List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
        if (arguments.isEmpty()) {
            throw new ProcessorException(msg(RETURN_OPTIONAL_ARG_EMPTY), element);
        }
        if (arguments.get(0).getKind() == TypeKind.WILDCARD) {
            throw new ProcessorException(msg(RETURN_OPTIONAL_ARG_WILDCARD), element);
        }
        return argumentValidator.validate(methodElement, arguments.get(0)).withOptional();
    }

}
