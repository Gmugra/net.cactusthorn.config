package net.cactusthorn.config.compiler.methodvalidator;

import static net.cactusthorn.config.compiler.methodvalidator.MethodInfo.StringMethod;
import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_STRING_CLASS;
import static javax.lang.model.element.ElementKind.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public class StringTypeValidator extends MethodValidatorAncestor {

    private final TypeMirror stringTM;
    private final TypeMirror runtimeExceptionTM;

    public StringTypeValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        stringTM = processingEnv.getElementUtils().getTypeElement(String.class.getName()).asType();
        runtimeExceptionTM = processingEnv.getElementUtils().getTypeElement(RuntimeException.class.getName()).asType();
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        if (processingEnv().getTypeUtils().isSameType(stringTM, typeMirror)) {
            return new MethodInfo(methodElement).withStringMethod(StringMethod.STRING, typeMirror);
        }

        TypeElement typeElement = (TypeElement) ((DeclaredType) typeMirror).asElement();

        Set<StringMethod> methods =
            typeElement.getEnclosedElements().stream().map(this::find).filter(sm -> sm != null).collect(Collectors.toSet());

        StringMethod stringMethod = null;
        if (typeElement.getKind() == ENUM && methods.contains(StringMethod.FROMSTRING)) {
            stringMethod = StringMethod.FROMSTRING;
        } else if (methods.contains(StringMethod.VALUEOF)) {
            stringMethod = StringMethod.VALUEOF;
        } else if (methods.contains(StringMethod.FROMSTRING)) {
            stringMethod = StringMethod.FROMSTRING;
        } else if (methods.contains(StringMethod.CONSTRUCTOR)) {
            stringMethod = StringMethod.CONSTRUCTOR;
        }

        if (stringMethod != null) {
            return new MethodInfo(methodElement).withStringMethod(stringMethod, typeMirror);
        }

        throw new ProcessorException(msg(RETURN_STRING_CLASS), typeElement);
    }

    private StringMethod find(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        boolean isStatic = modifiers.contains(Modifier.STATIC);
        boolean isPublic = modifiers.contains(Modifier.PUBLIC);
        if (element.getKind() == METHOD && isStatic && isPublic && checkParameter(element) && checkExceptions(element)) {
            String methodName = element.getSimpleName().toString();
            return StringMethod.METHODS.contains(methodName) ? StringMethod.fromMethodName(methodName) : null;
        }
        if (element.getKind() == CONSTRUCTOR && isPublic && checkParameter(element) && checkExceptions(element)) {
            return StringMethod.CONSTRUCTOR;
        }
        return null;
    }

    // Method must contain single String parameter
    private boolean checkParameter(Element methodElement) {
        List<? extends TypeMirror> parameters = ((ExecutableType) methodElement.asType()).getParameterTypes();
        if (parameters.size() == 1 && processingEnv().getTypeUtils().isSameType(stringTM, parameters.get(0))) {
            return true;
        }
        return false;
    }

    // Method can throws only RuntimeExceptions
    private boolean checkExceptions(Element methodElement) {
        List<? extends TypeMirror> throwns = ((ExecutableType) methodElement.asType()).getThrownTypes();
        return !throwns.stream().filter(tm -> !processingEnv().getTypeUtils().isAssignable(tm, runtimeExceptionTM)).findAny().isPresent();
    }
}
