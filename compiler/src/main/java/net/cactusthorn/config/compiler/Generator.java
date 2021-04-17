package net.cactusthorn.config.compiler;

import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;

public abstract class Generator {

    private final ClassName interfaceName;
    private final TypeElement interfaceElement;
    private final String packageName;
    private final String className;
    private final List<MethodInfo> methodsInfo;

    Generator(TypeElement interfaceElement, List<MethodInfo> methodsInfo, String classNamePrefix) {
        interfaceName = ClassName.get(interfaceElement);
        this.interfaceElement = interfaceElement;
        this.packageName = interfaceName.packageName();
        this.className = classNamePrefix + interfaceName.simpleName();
        this.methodsInfo = methodsInfo;
    }

    abstract JavaFile generate();

    protected TypeSpec.Builder classBuilder() {
        return TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    protected ClassName interfaceName() {
        return interfaceName;
    }

    protected TypeElement interfaceElement() {
        return interfaceElement;
    }

    protected String packageName() {
        return packageName;
    }

    protected String className() {
        return className;
    }

    protected List<MethodInfo> methodsInfo() {
        return methodsInfo;
    }
}
