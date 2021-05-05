package net.cactusthorn.config.compiler.configgenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

final class ValuesFieldPart implements GeneratorPart {

    @Override public void addPart(Builder classBuilder, Generator generator) {
        FieldSpec fieldSpec = FieldSpec.builder(CONCURRENTHASHMAP_STRING_OBJECT, VALUES_ATTR, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new ConcurrentHashMap<>()").build();
        classBuilder.addField(fieldSpec);
    }
}
