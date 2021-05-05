package net.cactusthorn.config.compiler.configbuildergenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.core.ConfigHolder;

public class ConstructorPart implements GeneratorPart {

    @Override public void addPart(Builder classBuilder, Generator generator) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(ConfigHolder.class, "configHolder", Modifier.FINAL).addStatement("super(configHolder)");
        classBuilder.addMethod(constructorBuilder.build());
    }

}
