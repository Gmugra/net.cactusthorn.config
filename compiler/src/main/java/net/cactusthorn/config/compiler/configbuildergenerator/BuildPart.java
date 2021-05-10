package net.cactusthorn.config.compiler.configbuildergenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Annotations;
import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo.StringMethod;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.util.ConfigBuilder;

public class BuildPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        ClassName configClass = ClassName.get(generator.packageName(),
                ConfigBuilder.CONFIG_CLASSNAME_PREFIX + generator.interfaceName().simpleName());

        MethodSpec.Builder buildBuilder = MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(configClass);

        addConfigHolder(buildBuilder, generator);

        addConverters(buildBuilder, generator.methodsInfo());

        buildBuilder.addStatement("$T<$T,$T> values = new $T<>()", Map.class, String.class, Object.class, HashMap.class);
        generator.methodsInfo().forEach(mi -> buildBuilder.addStatement("values.put($S, $L)", mi.key(), convert(mi)));

        classBuilder.addMethod(buildBuilder.addStatement("return new $T(values)", configClass).build());
    }

    private static final String CONFIG_HOLDER = "ch";

    private void addConfigHolder(MethodSpec.Builder buildBuilder, Generator generator) {

        Annotations.ConfigInfo configInfo = generator.interfaceInfo().configInfo();

        CodeBlock strategyBlock = CodeBlock.builder().add("$T.$L", LoadStrategy.class, configInfo.loadStrategy().name()).build();

        buildBuilder.addStatement("$T $L = loaders().load($L.class.getClassLoader(), $L, $L)", ConfigHolder.class, CONFIG_HOLDER,
                ConfigBuilder.CONFIG_CLASSNAME_PREFIX + generator.interfaceName().simpleName(), strategyBlock, URIS_ATTR);
    }

    private void addConverters(MethodSpec.Builder buildBuilder, List<MethodInfo> methodInfo) {
        Set<TypeMirror> converters = new HashSet<>();
        methodInfo.forEach(mi -> {
            if (mi.returnConverter().isPresent()) {
                TypeMirror converter = mi.returnConverter().get();
                if (!converters.contains(converter)) {
                    converters.add(converter);
                    buildBuilder.addStatement("CONVERTERS.computeIfAbsent($T.class, c -> new $T())", converter, converter);
                }
            }
        });
    }

    private CodeBlock convert(MethodInfo mi) {
        CodeBlock.Builder builder = findGetMethod(mi).add("(");
        CodeBlock defaultValue = defaultValue(mi);
        if (mi.returnConverter().isPresent()) {
            builder.add("s -> convert($T.class, s), ", mi.returnConverter().get());
            return builder.add("$S", mi.key()).add(split(mi)).add(defaultValue).add(")").build();
        } else if (mi.returnStringMethod().isPresent()) {
            MethodInfo.StringMethodInfo smi = mi.returnStringMethod().get();
            StringMethod sm = smi.stringMethod();
            if (sm == StringMethod.STRING) {
                builder.add("s -> s, ");
            } else if (sm == StringMethod.CONSTRUCTOR) {
                builder.add("s -> new $T(s), ", mi.returnTypeName());
            } else {
                builder.add("$T::$L, ", smi.methodType(), sm.methodName().get());
            }
            return builder.add("$S", mi.key()).add(split(mi)).add(defaultValue).add(")").build();
        }
        if (mi.returnTypeName().equals(TypeName.CHAR)) {
            builder.add("s -> s.charAt(0), ");
        } else {
            builder.add("$T::valueOf, ", mi.returnTypeName().box());
        }
        return builder.add("$S", mi.key()).add(defaultValue).add(")").build();
    }

    private CodeBlock.Builder findGetMethod(MethodInfo mi) {
        CodeBlock.Builder builder = CodeBlock.builder().add("$L.", CONFIG_HOLDER);
        if (mi.returnOptional()) {
            return builder.add(mi.returnInterface().map(t -> {
                if (t == List.class) {
                    return "getOptionalList";
                }
                if (t == Set.class) {
                    return "getOptionalSet";
                }
                return "getOptionalSortedSet";
            }).orElse("getOptional"));
        }
        return builder.add(mi.returnInterface().map(t -> {
            if (t == List.class) {
                return "getList";
            }
            if (t == Set.class) {
                return "getSet";
            }
            return "getSortedSet";
        }).orElse("get"));
    }

    private CodeBlock split(MethodInfo mi) {
        return mi.returnInterface().map(i -> CodeBlock.of(", $S", mi.split())).orElse(CodeBlock.of(""));
    }

    private CodeBlock defaultValue(MethodInfo mi) {
        return mi.defaultValue().map(s -> CodeBlock.of(", $S", s)).orElse(CodeBlock.of(""));
    }
}
