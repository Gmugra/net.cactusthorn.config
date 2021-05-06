package net.cactusthorn.config.compiler.configgenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;

final class GettersPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        generator.methodsInfo().forEach(mi -> addGetter(classBuilder, mi));
    }

    private void addGetter(TypeSpec.Builder classBuilder, MethodInfo methodInfo) {
        // @formatter:off
        MethodSpec getter =
            MethodSpec.methodBuilder(methodInfo.name())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build())
            .returns(methodInfo.returnTypeName())
            .addStatement("return ($T)$L.get($S)", methodInfo.returnTypeName(), VALUES_ATTR, methodInfo.key())
            .build();
        // @formatter:on
        classBuilder.addMethod(getter);
    }
}
