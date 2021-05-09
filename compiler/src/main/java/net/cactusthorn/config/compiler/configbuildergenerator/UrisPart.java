package net.cactusthorn.config.compiler.configbuildergenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Annotations;
import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

public class UrisPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        Annotations.ConfigInfo configInfo = generator.interfaceInfo().configInfo();

        CodeBlock.Builder block = CodeBlock.builder();
        for (int i = 0; i < configInfo.sources().length; i++) {
            if (i != 0) {
                block.add(", ");
            }
            block.add("$S", configInfo.sources()[i]);
        }

        FieldSpec fieldSpec = FieldSpec.builder(ArrayTypeName.of(STRING), URIS_ATTR, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T[] {$L}", String.class, block.build()).build();
        classBuilder.addField(fieldSpec);
    }
}
