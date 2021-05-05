package net.cactusthorn.config.compiler.configgenerator;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.InterfaceInfo;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;

public final class ConfigGenerator extends Generator {

    public static final String CLASSNAME_PREFIX = "Config$$";

    private static final List<GeneratorPart> PARTS = Arrays.asList(new SerialVersionUIDPart(), new ValuesFieldPart(), new ConstructorPart(),
            new GettersPart(), new HashCodePart(), new ToStringPart(), new EqualsPart());

    public ConfigGenerator(TypeElement interfaceElement, List<MethodInfo> methodsInfo, InterfaceInfo interfaceInfo) {
        super(interfaceElement, methodsInfo, CLASSNAME_PREFIX, interfaceInfo);
    }

    @Override public JavaFile generate() {
        TypeSpec.Builder classBuilder = classBuilder().addSuperinterface(interfaceElement().asType());
        PARTS.forEach(p -> p.addPart(classBuilder, this));
        return JavaFile.builder(packageName(), classBuilder.build()).build();
    }
}
