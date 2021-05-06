package net.cactusthorn.config.compiler.configbuildergenerator;

import java.util.HashMap;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

final class DefaultPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {

        FieldSpec fieldSpec = FieldSpec.builder(MAP_STRING_STRING, DEFAULTS_ATTR, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .build();

        CodeBlock.Builder blockBuilder = CodeBlock.builder().addStatement("$L = new $T<>()", DEFAULTS_ATTR, HashMap.class);
        generator.methodsInfo()
                .forEach(mi -> mi.defaultValue().ifPresent(d -> blockBuilder.addStatement("$L.put($S, $S)", DEFAULTS_ATTR, mi.name(), d)));

        classBuilder.addField(fieldSpec);
        classBuilder.addStaticBlock(blockBuilder.build());
    }
}
