package net.cactusthorn.config.compiler;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class MethodValidator {

    static final List<Class<?>> INTERFACES = Arrays.asList(List.class, Set.class, SortedSet.class);

    private final ProcessingEnvironment processingEnv;

    private final TypeMirror stringTM;
    private final TypeMirror optionalTM;
    private final Set<TypeMirror> interfacesTM;

    MethodValidator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        stringTM = processingEnv.getElementUtils().getTypeElement(String.class.getName()).asType();
        optionalTM = processingEnv.getElementUtils().getTypeElement(Optional.class.getName()).asType();
        // @formatter:off
        interfacesTM = INTERFACES.stream()
            .map(i -> processingEnv.getElementUtils().getTypeElement(i.getName()).asType())
            .collect(Collectors.toSet());
        // @formatter:on
    }

    void validate(final Element methodElement) {
        TypeMirror returnTypeMirror = ((ExecutableType) methodElement.asType()).getReturnType();
        if (returnTypeMirror.getKind() == TypeKind.VOID) {
            throw new ProcessorException(msg(RETURN_VOID), methodElement);
        }
        if (returnTypeMirror.getKind() == TypeKind.DECLARED) {
            DeclaredType returnDeclaredType = (DeclaredType) returnTypeMirror;
            // @formatter:off
            ((Predicate<DeclaredType>) this::checkInterface)
                .or(this::checkAbstract)
                .or(this::checkOptional)
                .or(this::checkStringMethod)
                .test(returnDeclaredType);
            // @formatter:off
        }
    }

    private boolean checkAbstract(DeclaredType declaredType) {
        Element element = declaredType.asElement();
        if (element.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessorException(msg(RETURN_ABSTRACT), element);
        }
        return false;
    }

    private boolean checkInterface(DeclaredType declaredType) {
        Element element = declaredType.asElement();
        if (element.getKind() != ElementKind.INTERFACE) {
            return false;
        }
        TypeMirror typeMirror = element.asType();
        if (!interfacesTM.stream().filter(t -> processingEnv.getTypeUtils().isSameType(typeMirror, t)).findAny().isPresent()) {
            throw new ProcessorException(msg(RETURN_INTERFACES, INTERFACES), element);
        }
        List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
        if (arguments.isEmpty()) {
            throw new ProcessorException(msg(RETURN_INTERFACE_ARG_EMPTY), element);
        }
        if (arguments.get(0).getKind() == TypeKind.WILDCARD) {
            throw new ProcessorException(msg(RETURN_INTERFACE_ARG_WILDCARD), element);
        }
        if (arguments.get(0).getKind() == TypeKind.DECLARED) {
            DeclaredType argumentDeclaredType = (DeclaredType) arguments.get(0);
            Element argumentElement = argumentDeclaredType.asElement();
            if (argumentElement.getKind() == ElementKind.INTERFACE) {
                throw new ProcessorException(msg(RETURN_INTERFACE_ARG_INTERFACE), element);
            }
            checkAbstract(argumentDeclaredType);
            checkStringMethod(argumentDeclaredType);
        }
        return true;
    }

    private boolean checkOptional(DeclaredType declaredType) {
        Element element = declaredType.asElement();
        TypeMirror typeMirror = element.asType();
        if (!processingEnv.getTypeUtils().isSameType(optionalTM, typeMirror)) {
            return false;
        }
        List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
        if (arguments.isEmpty()) {
            throw new ProcessorException(msg(RETURN_OPTIONAL_ARG_EMPTY), element);
        }
        if (arguments.get(0).getKind() == TypeKind.WILDCARD) {
            throw new ProcessorException(msg(RETURN_OPTIONAL_ARG_WILDCARD), element);
        }
        if (arguments.get(0).getKind() == TypeKind.DECLARED) {
            // @formatter:off
            ((Predicate<DeclaredType>) this::checkInterface)
                .or(this::checkAbstract)
                .or(this::checkStringMethod)
                .test((DeclaredType) arguments.get(0));
            // @formatter:on
        }
        return true;
    }

    private static final Set<String> STRING_METHODS = new HashSet<>(Arrays.asList("valueOf", "fromString"));

    private boolean checkStringMethod(DeclaredType declaredType) {
        Predicate<Element> check = e -> {
            if (e.getKind() == ElementKind.METHOD && e.getModifiers().contains(Modifier.STATIC)
                    && STRING_METHODS.contains(e.getSimpleName().toString()) && existsSingleStringParameter(e)) {
                return true;
            }
            if (e.getKind() == ElementKind.CONSTRUCTOR && existsSingleStringParameter(e)) {
                return true;
            }
            return false;
        };
        Element element = declaredType.asElement();
        ((TypeElement) element).getEnclosedElements().stream().filter(check).findAny()
                .orElseThrow(() -> new ProcessorException(msg(RETURN_STRING_CLASS), element));
        return true;
    }

    private boolean existsSingleStringParameter(Element methodElement) {
        List<? extends TypeMirror> parameters = ((ExecutableType) methodElement.asType()).getParameterTypes();
        if (parameters.size() == 1 && processingEnv.getTypeUtils().isSameType(stringTM, parameters.get(0))) {
            return true;
        }
        return false;
    }
}
