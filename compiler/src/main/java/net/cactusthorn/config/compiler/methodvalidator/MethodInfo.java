package net.cactusthorn.config.compiler.methodvalidator;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import net.cactusthorn.config.compiler.InterfaceInfo;
import net.cactusthorn.config.core.Key;

public class MethodInfo {

    public enum StringMethod {
        STRING(Optional.empty()), VALUEOF(Optional.of("valueOf")), FROMSTRING(Optional.of("fromString")), CONSTRUCTOR(Optional.empty());

        public static final Set<String> METHODS = Stream.of(values()).map(sm -> sm.methodName()).filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet());

        private final Optional<String> methodName;

        StringMethod(Optional<String> methodName) {
            this.methodName = methodName;
        }

        public Optional<String> methodName() {
            return methodName;
        }

        public static StringMethod fromMethodName(String name) {
            // @formatter:off
            return Stream.of(values())
                .filter(sm -> sm.methodName().filter(name::equals).isPresent())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
            // @formatter:on
        }
    }

    public static final class StringMethodInfo {
        private final StringMethod stringMethod;
        private final TypeName methodType;

        private StringMethodInfo(StringMethod stringMethod, TypeMirror stringMethodTM) {
            this.stringMethod = stringMethod;
            this.methodType = TypeName.get(stringMethodTM);
        }

        public StringMethod stringMethod() {
            return stringMethod;
        }

        public TypeName methodType() {
            return methodType;
        }
    }


    private final ExecutableElement methodElement;
    private final TypeName returnTypeName;
    private final String name;
    private final String split;

    private String key;

    private Optional<StringMethodInfo> returnStringMethod = Optional.empty();
    private Optional<Type> returnInterface = Optional.empty();
    private boolean returnOptional = false;

    MethodInfo(ExecutableElement methodElement) {
        this.methodElement = methodElement;
        returnTypeName = ClassName.get(methodElement.getReturnType());
        name = methodElement.getSimpleName().toString();

        key = name;

        split = ",";
    }

    MethodInfo withStringMethod(StringMethod stringMethod, TypeMirror stringMethodTM) {
        returnStringMethod = Optional.of(new StringMethodInfo(stringMethod, stringMethodTM));
        return this;
    }

    MethodInfo withInterface(Optional<Type> interfaceType) {
        returnInterface = interfaceType;
        return this;
    }

    public MethodInfo withInterfaceInfo(InterfaceInfo interfaceInfo) {
        key = findKey(interfaceInfo);
        return this;
    }

    private String findKey(InterfaceInfo interfaceInfo) {
        Key[] keyAnnotations = methodElement.getAnnotationsByType(Key.class);
        if (keyAnnotations.length != 0) {
            return interfaceInfo.prefix() + keyAnnotations[0].value() + Key.KEY_SEPARATOR + name;
        }
        return interfaceInfo.prefix() + name;
    }

    MethodInfo withOptional() {
        returnOptional = true;
        return this;
    }

    public String key() {
        return key;
    }

    public String name() {
        return name;
    }

    public TypeName returnTypeName() {
        return returnTypeName;
    }

    public Optional<Type> returnInterface() {
        return returnInterface;
    }

    public boolean returnOptional() {
        return returnOptional;
    }

    public Optional<StringMethodInfo> returnStringMethod() {
        return returnStringMethod;
    }

    public String split() {
        return split;
    }
}
