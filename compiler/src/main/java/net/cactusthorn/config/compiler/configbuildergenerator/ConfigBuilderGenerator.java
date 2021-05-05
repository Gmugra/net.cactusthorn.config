package net.cactusthorn.config.compiler.configbuildergenerator;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.InterfaceInfo;
import net.cactusthorn.config.compiler.configgenerator.ConfigGenerator;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;
import net.cactusthorn.config.core.ConfigBuilder;

public final class ConfigBuilderGenerator extends Generator {

    private static final List<GeneratorPart> PARTS = Arrays.asList(new DefaultPart(), new ConstructorPart(), new BuildPart());

    public ConfigBuilderGenerator(TypeElement interfaceElement, List<MethodInfo> methodsInfo, InterfaceInfo interfaceInfo) {
        super(interfaceElement, methodsInfo, ConfigBuilder.BUILDER_CLASSNAME_PREFIX, interfaceInfo);
    }

    private static final ClassName CONFIG_BUILDER = ClassName.get(ConfigBuilder.class);

    @Override public JavaFile generate() {
        ClassName configClass = ClassName.get(packageName(), ConfigGenerator.CLASSNAME_PREFIX + interfaceName().simpleName());
        TypeName superClass = ParameterizedTypeName.get(CONFIG_BUILDER, configClass);
        TypeSpec.Builder classBuilder = classBuilder().superclass(superClass);
        PARTS.forEach(p -> p.addPart(classBuilder, this));

        return JavaFile.builder(packageName(), classBuilder.build()).build();
    }
}
