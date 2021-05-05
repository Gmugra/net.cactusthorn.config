package net.cactusthorn.config.compiler.configgenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;

final class ToStringPart implements GeneratorPart {

    private static final String BUF_NAME = "buf";

    @Override public void addPart(Builder classBuilder, Generator generator) {
        // @formatter:off
        MethodSpec.Builder toStringBuilder =
            MethodSpec.methodBuilder("toString")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(String.class)
            .addStatement("$T $L = new $T()", StringBuilder.class, BUF_NAME, StringBuilder.class)
            .addStatement("$L.append('[')", BUF_NAME);
        // @formatter:on
        for (int i = 0; i < generator.methodsInfo().size(); i++) {
            if (i != 0) {
                toStringBuilder.addStatement("$L.append($S)", BUF_NAME, ", ");
            }
            MethodInfo mi = generator.methodsInfo().get(i);
            toStringBuilder.addStatement("$L.append($S).append('=').append($T.valueOf($L.get($S)))", BUF_NAME, mi.name(), String.class,
                    VALUES_ATTR, mi.key());
        }
        toStringBuilder.addStatement("$L.append(']')", BUF_NAME);
        toStringBuilder.addStatement("return $L.toString()", BUF_NAME);
        classBuilder.addMethod(toStringBuilder.build());
    }

}
