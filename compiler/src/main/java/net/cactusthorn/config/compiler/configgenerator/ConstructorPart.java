package net.cactusthorn.config.compiler.configgenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

final class ConstructorPart implements GeneratorPart {

    private static final String VALUES_PARAM = "values";

    @Override public void addPart(Builder classBuilder, Generator generator) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addParameter(MAP_STRING_OBJECT, VALUES_PARAM, Modifier.FINAL).addStatement("$L.putAll($L)", VALUES_ATTR, VALUES_PARAM);
        classBuilder.addMethod(constructorBuilder.build());
    }
}
