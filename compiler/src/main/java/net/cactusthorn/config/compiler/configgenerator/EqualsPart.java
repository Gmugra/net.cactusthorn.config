package net.cactusthorn.config.compiler.configgenerator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

final class EqualsPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        // @formatter:off
        MethodSpec.Builder equalsBuilder =
            MethodSpec.methodBuilder("equals")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(boolean.class)
            .addParameter(ParameterSpec.builder(Object.class, "o").build())
            .addStatement("if (o == this) return true")
            .addStatement("if (!(o instanceof $L)) return false", generator.className())
            .addStatement("$L other = ($L) o", generator.className(), generator.className());
        // @formatter:on
        generator.methodsInfo().forEach(mi -> {
            if (mi.returnTypeName().isPrimitive()) {
                equalsBuilder.addStatement("if (this.$L() != other.$L()) return false", mi.name(), mi.name());
            } else {
                equalsBuilder.addStatement("if (!this.$L().equals(other.$L())) return false", mi.name(), mi.name());
            }
        });
        equalsBuilder.addStatement("return true");
        classBuilder.addMethod(equalsBuilder.build());
    }
}
