package net.cactusthorn.config.compiler.methodvalidator;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.lang.reflect.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public class InterfaceTypeValidator extends MethodValidatorAncestor {

    public static final List<Class<?>> INTERFACES = Arrays.asList(List.class, Set.class, SortedSet.class);

    private final Map<TypeMirror, Type> interfaces = new HashMap<>();

    private final MethodValidator argumentValidator;

    public InterfaceTypeValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        INTERFACES.forEach(c -> interfaces.put(processingEnv().getElementUtils().getTypeElement(c.getName()).asType(), c));
        // @formatter:off
        argumentValidator = MethodValidatorChain.builder(processingEnv, AbstractTypeValidator.class)
                .next(DefaultConvertorValidator.class)
                .next(ConverterValidator.class)
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
        if (element.getKind() != ElementKind.INTERFACE) {
            return next(methodElement, typeMirror);
        }
        // @formatter:off
        Type interfaceType =
            interfaces.entrySet().stream()
            .filter(e -> processingEnv().getTypeUtils().isSameType(element.asType(), e.getKey()))
            .map(e -> e.getValue())
            .findAny()
            .orElseThrow(() -> new ProcessorException(msg(RETURN_INTERFACES, INTERFACES), element));
        // @formatter:on
        List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
        if (arguments.isEmpty()) {
            throw new ProcessorException(msg(RETURN_INTERFACE_ARG_EMPTY), element);
        }
        if (arguments.get(0).getKind() == TypeKind.WILDCARD) {
            throw new ProcessorException(msg(RETURN_INTERFACE_ARG_WILDCARD), element);
        }
        DeclaredType argumentDeclaredType = (DeclaredType) arguments.get(0);
        Element argumentElement = argumentDeclaredType.asElement();
        if (argumentElement.getKind() == ElementKind.INTERFACE) {
            throw new ProcessorException(msg(RETURN_INTERFACE_ARG_INTERFACE), element);
        }
        return argumentValidator.validate(methodElement, arguments.get(0)).withInterface(interfaceType);
    }
}
