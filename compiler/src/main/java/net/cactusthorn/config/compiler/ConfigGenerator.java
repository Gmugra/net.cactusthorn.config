package net.cactusthorn.config.compiler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;

final class ConfigGenerator extends Generator {

    static final String CLASSNAME_PREFIX = "Config$$";

    ConfigGenerator(TypeElement interfaceElement, List<MethodInfo> methodsInfo) {
        super(interfaceElement, methodsInfo, CLASSNAME_PREFIX);
    }

    @Override JavaFile generate() {
        TypeSpec.Builder classBuilder = classBuilder().addSuperinterface(interfaceElement().asType());
        addEnum(classBuilder);
        addValues(classBuilder);
        addConstructor(classBuilder);
        methodsInfo().forEach(mi -> addGetter(classBuilder, mi));
        addHashCode(classBuilder);
        addEquals(classBuilder);
        addToString(classBuilder);

        return JavaFile.builder(packageName(), classBuilder.build()).build();
    }

    static final String METHOD_ENUM_NAME = "Method";
    static final ClassName METHOD_ENUM = ClassName.get("", METHOD_ENUM_NAME);

    private void addEnum(TypeSpec.Builder classBuilder) {
        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(METHOD_ENUM_NAME).addModifiers(Modifier.PUBLIC);
        methodsInfo().forEach(mi -> enumBuilder.addEnumConstant(mi.name()));
        classBuilder.addType(enumBuilder.build());
    }

    private static final ClassName CONCURRENTHASHMAP = ClassName.get(ConcurrentHashMap.class);
    private static final ClassName OBJECT = ClassName.get(Object.class);
    private static final TypeName VALUES_MAP = ParameterizedTypeName.get(CONCURRENTHASHMAP, METHOD_ENUM, OBJECT);

    private static final String VALUES_ATTR = "VALUES";

    private void addValues(TypeSpec.Builder classBuilder) {
        FieldSpec fieldSpec = FieldSpec.builder(VALUES_MAP, VALUES_ATTR, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new ConcurrentHashMap<>()").build();
        classBuilder.addField(fieldSpec);
    }

    private static final ClassName MAP = ClassName.get(Map.class);
    private static final TypeName VALUES_PARAM_MAP = ParameterizedTypeName.get(MAP, METHOD_ENUM, OBJECT);

    private static final String VALUES_PARAM = "values";

    private void addConstructor(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addParameter(VALUES_PARAM_MAP, VALUES_PARAM, Modifier.FINAL)
                .addStatement("$L.putAll($L)", VALUES_ATTR, VALUES_PARAM);
        classBuilder.addMethod(constructorBuilder.build());
    }

    private void addGetter(TypeSpec.Builder classBuilder, MethodInfo methodInfo) {
        // @formatter:off
        MethodSpec getter =
            MethodSpec.methodBuilder(methodInfo.name())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(methodInfo.returnTypeName())
            .addStatement("return ($T)VALUES.get(Method.$L)", methodInfo.returnTypeName(), methodInfo.name())
            .build();
        // @formatter:on
        classBuilder.addMethod(getter);
    }

    private void addHashCode(TypeSpec.Builder classBuilder) {
        String parameters = methodsInfo().stream().map(mi -> mi.name() + "()").collect(Collectors.joining(", "));
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

    private static final String BUF_NAME = "buf";

    private void addToString(TypeSpec.Builder classBuilder) {
        // @formatter:off
        MethodSpec.Builder toStringBuilder =
            MethodSpec.methodBuilder("toString")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(String.class)
            .addStatement("$T $L = new $T()", StringBuilder.class, BUF_NAME, StringBuilder.class)
            .addStatement("$L.append('[')", BUF_NAME);
        // @formatter:on
        for (int i = 0; i < methodsInfo().size(); i++) {
            if (i != 0) {
                toStringBuilder.addStatement("$L.append($S)", BUF_NAME, ", ");
            }
            MethodInfo mi = methodsInfo().get(i);
            toStringBuilder.addStatement("$L.append($L.$L).append('=').append($T.valueOf($L.get($L.$L)))", BUF_NAME, METHOD_ENUM, mi.name(),
                    String.class, VALUES_ATTR, METHOD_ENUM, mi.name());
        }
        toStringBuilder.addStatement("$L.append(']')", BUF_NAME);
        toStringBuilder.addStatement("return $L.toString()", BUF_NAME);
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
            .addStatement("if (!(o instanceof $L)) return false", className())
            .addStatement("$L other = ($L) o", className(), className());
        // @formatter:on
        methodsInfo().forEach(mi -> {
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
