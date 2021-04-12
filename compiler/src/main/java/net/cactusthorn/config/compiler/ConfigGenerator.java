package net.cactusthorn.config.compiler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

final class ConfigGenerator {

    private final TypeElement interfaceElement;
    private final String packageName;
    private final String className;
    private final List<MethodInfo> methodsInfo;

    ConfigGenerator(TypeElement interfaceElement, List<MethodInfo> methodsInfo) {
        ClassName interfaceName = ClassName.get(interfaceElement);
        this.interfaceElement = interfaceElement;
        this.packageName = interfaceName.packageName();
        this.className = "Config$$" + interfaceName.simpleName();
        this.methodsInfo = methodsInfo;
    }

    JavaFile generate() {
        TypeSpec.Builder classBuilder = classBuilder();
        methodsInfo.forEach(mi -> addField(classBuilder, mi));
        addConstructor(classBuilder);
        methodsInfo.forEach(mi -> addGetter(classBuilder, mi));
        addHashCode(classBuilder);
        addEquals(classBuilder);
        addToString(classBuilder);

        return JavaFile.builder(packageName, classBuilder.build()).build();
    }

    private TypeSpec.Builder classBuilder() {
        return TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC, Modifier.FINAL).addSuperinterface(interfaceElement.asType());
    }

    private void addField(TypeSpec.Builder classBuilder, MethodInfo methodInfo) {
        FieldSpec fieldSpec = FieldSpec.builder(methodInfo.returnTypeName(), methodInfo.name(), Modifier.PRIVATE, Modifier.FINAL).build();
        classBuilder.addField(fieldSpec);
    }

    private void addConstructor(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
        methodsInfo.forEach(mi -> constructorBuilder.addParameter(mi.returnTypeName(), mi.name(), Modifier.FINAL));
        methodsInfo.forEach(mi -> constructorBuilder.addStatement("this.$N = $N", mi.name(), mi.name()));
        classBuilder.addMethod(constructorBuilder.build());
    }

    private void addGetter(TypeSpec.Builder classBuilder, MethodInfo methodInfo) {
        // @formatter:off
        MethodSpec getter =
            MethodSpec.methodBuilder(methodInfo.name())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(methodInfo.returnTypeName())
            .addStatement("return $N", methodInfo.name())
            .build();
        // @formatter:on
        classBuilder.addMethod(getter);
    }

    private void addHashCode(TypeSpec.Builder classBuilder) {
        String parameters = methodsInfo.stream().map(mi -> mi.name()).collect(Collectors.joining(", "));
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

    private void addToString(TypeSpec.Builder classBuilder) {
        // @formatter:off
        MethodSpec.Builder toStringBuilder =
            MethodSpec.methodBuilder("toString")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(String.class)
            .addStatement("$T buf = new $T()", StringBuilder.class, StringBuilder.class)
            .addStatement("buf.append($S)", "[");
        // @formatter:on
        for (int i = 0; i < methodsInfo.size(); i++) {
            if (i != 0) {
                toStringBuilder.addStatement("buf.append($S)", ", ");
            }
            MethodInfo mi = methodsInfo.get(i);
            toStringBuilder.addStatement("buf.append($S).append($S).append($T.valueOf($L))", mi.name(), "=", String.class, mi.name());
        }
        toStringBuilder.addStatement("buf.append($S)", "]");
        toStringBuilder.addStatement("return buf.toString()");
        classBuilder.addMethod(toStringBuilder.build());
    }

    private void addEquals(TypeSpec.Builder classBuilder) {
        // @formatter:off
        MethodSpec.Builder equalsBuilder =
            MethodSpec.methodBuilder("equals")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(boolean.class)
            .addParameter(ParameterSpec.builder(Object.class, "o").build())
            .addStatement("if (o == this) return true")
            .addStatement("if (!(o instanceof $L)) return false", className)
            .addStatement("$L other = ($L) o", className, className);
        // @formatter:on
        methodsInfo.forEach(mi -> {
            if (mi.returnTypeName().isPrimitive()) {
                equalsBuilder.addStatement("if (this.$L != other.$L()) return false", mi.name(), mi.name());
            } else {
                equalsBuilder.addStatement("if (!this.$L.equals(other.$L())) return false", mi.name(), mi.name());
            }
        });
        equalsBuilder.addStatement("return true");
        classBuilder.addMethod(equalsBuilder.build());
    }
}
