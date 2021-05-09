package net.cactusthorn.config.compiler.configbuildergenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.core.loader.Loaders;

public class ConstructorPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(Loaders.class, "loaders", Modifier.FINAL).addStatement("super(loaders)");
        classBuilder.addMethod(constructorBuilder.build());
    }

}
