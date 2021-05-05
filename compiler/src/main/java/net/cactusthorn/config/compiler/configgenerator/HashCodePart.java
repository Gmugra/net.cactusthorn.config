package net.cactusthorn.config.compiler.configgenerator;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

final class HashCodePart implements GeneratorPart {

    @Override public void addPart(Builder classBuilder, Generator generator) {
        String parameters = generator.methodsInfo().stream().map(mi -> mi.name() + "()").collect(Collectors.joining(", "));
        // @formatter:off
        MethodSpec hashCode =
            MethodSpec.methodBuilder("hashCode")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(int.class)
            .addStatement("return $T.hash($L)", Objects.class, parameters)
            .build();
        // @formatter:on
        classBuilder.addMethod(hashCode);
    }
}
