package net.cactusthorn.config.compiler;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo.StringMethod;
import net.cactusthorn.config.core.ConfigBuilder;

public final class ConfigBuilderGenerator extends Generator {

    private final ClassName configClass;

    ConfigBuilderGenerator(TypeElement interfaceElement, List<MethodInfo> methodsInfo) {
        super(interfaceElement, methodsInfo, ConfigBuilder.BUILDER_CLASSNAME_PREFIX);
        configClass = ClassName.get(packageName(), ConfigGenerator.CLASSNAME_PREFIX + interfaceName().simpleName());
    }

    private static final ClassName CONFIG_BUILDER = ClassName.get(ConfigBuilder.class);

    @Override JavaFile generate() {
        TypeName superClass = ParameterizedTypeName.get(CONFIG_BUILDER, configClass);
        TypeSpec.Builder classBuilder = classBuilder().superclass(superClass);
        addEnum(classBuilder);
        addKey(classBuilder);
        addConstructor(classBuilder);
        addBuild(classBuilder);

        return JavaFile.builder(packageName(), classBuilder.build()).build();
    }

    private static final String METHOD_ENUM_NAME = "Method";

    private void addEnum(TypeSpec.Builder classBuilder) {
        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(METHOD_ENUM_NAME).addModifiers(Modifier.PRIVATE);
        methodsInfo().forEach(mi -> enumBuilder.addEnumConstant(mi.name()));
        classBuilder.addType(enumBuilder.build());
    }

    private static final ClassName MAP = ClassName.get(Map.class);
    private static final ClassName STRING = ClassName.get(String.class);
    private static final TypeName MAP_STRING = ParameterizedTypeName.get(MAP, STRING, STRING);

    private static final String KEYS_MAP_NAME = "KEYS";

    private void addKey(TypeSpec.Builder classBuilder) {
        ClassName methodsEnumName = ClassName.get(packageName(), className() + '.' + METHOD_ENUM_NAME);
        TypeName mapTypeName = ParameterizedTypeName.get(MAP, methodsEnumName, STRING);

        FieldSpec fieldSpec = FieldSpec.builder(mapTypeName, KEYS_MAP_NAME, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).build();

        CodeBlock.Builder blockBuilder = CodeBlock.builder().addStatement("$L = new $T<>($L.class)", KEYS_MAP_NAME, EnumMap.class,
                METHOD_ENUM_NAME);
        methodsInfo().forEach(mi -> blockBuilder.addStatement("$L.put($L.$L, $S)", KEYS_MAP_NAME, METHOD_ENUM_NAME, mi.name(), mi.key()));

        classBuilder.addField(fieldSpec);
        classBuilder.addStaticBlock(blockBuilder.build());
    }

    private static final String PROPERTIES = "properties";

    private void addConstructor(TypeSpec.Builder classBuilder) {
        // @formatter:off
        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(MAP_STRING, PROPERTIES, Modifier.FINAL)
            .addStatement("super($L)", PROPERTIES);
        // @formatter:on

        classBuilder.addMethod(constructorBuilder.build());
    }

    private void addBuild(TypeSpec.Builder classBuilder) {
        // @formatter:off
        MethodSpec.Builder buildBuilder =
            MethodSpec.methodBuilder("build")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(configClass);
        // @formatter:on

        methodsInfo().forEach(mi -> buildBuilder.addStatement("$T $L = $L", mi.returnTypeName(), mi.name(), convert(mi)));

        String parameters = methodsInfo().stream().map(mi -> mi.name()).collect(Collectors.joining(", "));
        buildBuilder.addStatement("return new $T($L)", configClass, parameters);

        classBuilder.addMethod(buildBuilder.build());
    }

    private CodeBlock convert(MethodInfo mi) {
        String getMethod = findGetMethod(mi.returnOptional(), mi.returnInterface());
        CodeBlock.Builder builder = CodeBlock.builder().add(getMethod + '(');
        if (mi.returnStringMethod().isPresent()) {
            MethodInfo.StringMethodInfo smi = mi.returnStringMethod().get();
            StringMethod sm = smi.stringMethod();
            CodeBlock split = split(mi);
            switch (sm) {
            case STRING:
                return builder.add("s -> s, ").add(key(mi)).add(split).add(")").build();
            case CONSTRUCTOR:
                return builder.add("s -> new $T(s), ", mi.returnTypeName()).add(key(mi)).add(split).add(")").build();
            default:
                return builder.add("$T::$L, ", smi.methodType(), sm.methodName().get()).add(key(mi)).add(split).add(")").build();
            }
        }
        TypeName simpleType = mi.returnTypeName().isPrimitive() ? mi.returnTypeName().box() : mi.returnTypeName();
        return builder.add("$T::$L, ", simpleType, "valueOf").add(key(mi)).add(")").build();
    }

    protected CodeBlock split(MethodInfo mi) {
        if (mi.returnInterface().isPresent()) {
            return CodeBlock.of(", $S", mi.split());
        }
        return CodeBlock.of("");
    }

    protected String findGetMethod(boolean optional, Optional<Type> returnInterface) {
        if (optional) {
            if (returnInterface.isPresent()) {
                Type type = returnInterface.get();
                if (type == List.class) {
                    return "getOptionalList";
                }
                if (type == Set.class) {
                    return "getOptionalSet";
                }
                return "getOptionalSortedSet";
            }
            return "getOptional";
        }
        if (returnInterface.isPresent()) {
            Type type = returnInterface.get();
            if (type == List.class) {
                return "getList";
            }
            if (type == Set.class) {
                return "getSet";
            }
            return "getSortedSet";
        }
        return "get";
    }

    private CodeBlock key(MethodInfo mi) {
        return CodeBlock.of("$L.get($L.$L)", KEYS_MAP_NAME, METHOD_ENUM_NAME, mi.name());
    }
}
