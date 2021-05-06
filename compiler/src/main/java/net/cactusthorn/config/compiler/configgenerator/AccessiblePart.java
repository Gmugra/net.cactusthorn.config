package net.cactusthorn.config.compiler.configgenerator;

import java.util.Collections;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

public class AccessiblePart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        if (!generator.interfaceInfo().accessible()) {
            return;
        }
        addKeys(classBuilder);
        addGet(classBuilder);
        addAsMap(classBuilder);
    }

    private void addKeys(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder keysBuilder = MethodSpec.methodBuilder("keys").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(SET_STRING).addStatement("return $T.unmodifiableSet($L.keySet())", Collections.class, VALUES_ATTR);
        classBuilder.addMethod(keysBuilder.build());
    }

    private void addGet(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder keysBuilder = MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(Object.class).addParameter(String.class, "key").addStatement("return $L.get(key)", VALUES_ATTR);
        classBuilder.addMethod(keysBuilder.build());
    }

    private void addAsMap(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder keysBuilder = MethodSpec.methodBuilder("asMap").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(MAP_STRING_OBJECT).addStatement("return $T.unmodifiableMap($L)", Collections.class, VALUES_ATTR);
        classBuilder.addMethod(keysBuilder.build());
    }
}
