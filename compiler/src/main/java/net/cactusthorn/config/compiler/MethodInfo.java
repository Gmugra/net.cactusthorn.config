package net.cactusthorn.config.compiler;

import javax.lang.model.element.ExecutableElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

final class MethodInfo {

    private final String key;
    private final String name;
    private final TypeName returnTypeName;

    MethodInfo(ExecutableElement methodElement) {
        name = methodElement.getSimpleName().toString();
        returnTypeName = ClassName.get(methodElement.getReturnType());
        key = name;
    }

    String key() {
        return key;
    }

    String name() {
        return name;
    }

    TypeName returnTypeName() {
        return returnTypeName;
    }
}
