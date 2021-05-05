package net.cactusthorn.config.compiler.configgenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

final class SerialVersionUIDPart implements GeneratorPart {

    @Override public void addPart(Builder classBuilder, Generator generator) {
        generator.interfaceInfo().serialVersionUID().ifPresent(svuid -> {
            FieldSpec fieldSpec = FieldSpec.builder(TypeName.LONG, "serialVersionUID", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$LL", svuid).build();
            classBuilder.addField(fieldSpec);
        });
    }

}
