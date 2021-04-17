package net.cactusthorn.config.compiler;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.METHOD_MUST_EXIST;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.ONLY_INTERFACE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.cactusthorn.config.compiler.methodvalidator.*;
import net.cactusthorn.config.core.Config;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import com.squareup.javapoet.JavaFile;

public final class ConfigProcessor extends AbstractProcessor {

    public static final Comparator<MethodInfo> METHODINFO_COMPARATOR = (mi1, mi2) -> {
        if (mi1 == null && mi2 == null) {
            return 0;
        }
        if (mi1 == null) {
            return 1;
        }
        if (mi2 == null) {
            return -1;
        }
        return mi1.name().compareTo(mi2.name());
    };

    private static final Set<String> SUPPORTED_ANNOTATIONS = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("net.cactusthorn.config.core.Config")));

    @Override public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private MethodValidator typeValidator;

    private List<ExecutableElement> objectMethods;

    @Override public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        objectMethods = ElementFilter
                .methodsIn(processingEnv.getElementUtils().getTypeElement(Object.class.getName()).getEnclosedElements());

        // @formatter:off
        typeValidator =
            MethodValidatorChain.builder(processingEnv, WithoutParametersValidator.class)
            .next(ReturnVoidValidator.class)
            .next(InterfaceTypeValidator.class)
            .next(AbstractTypeValidator.class)
            .next(OptionalTypeValidator.class)
            .next(StringTypeValidator.class)
            .build();
        // @formatter:on
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Config.class);
            for (Element element : elements) {
                validateInterface(element);

                TypeElement interfaceType = (TypeElement) element;
                // @formatter:off
                Set<ExecutableElement> allMethodsElements =
                     ElementFilter.methodsIn(processingEnv.getElementUtils().getAllMembers(interfaceType))
                     .stream()
                     .filter(e -> !objectMethods.contains(e))
                     .collect(Collectors.toSet());
                // @formatter:on

                validateMethodExist(element, allMethodsElements);

                List<MethodInfo> methodsInfo = new ArrayList<>();
                allMethodsElements.forEach(m -> {
                    TypeMirror returnTypeMirror = m.getReturnType();
                    methodsInfo.add(typeValidator.validate(m, returnTypeMirror));
                });

                Collections.sort(methodsInfo, METHODINFO_COMPARATOR);

                JavaFile configFile = new ConfigGenerator(interfaceType, methodsInfo).generate();
                //System.out.println(configFile.toString());
                configFile.writeTo(processingEnv.getFiler());

                JavaFile configBuilderFile = new ConfigBuilderGenerator(interfaceType, methodsInfo).generate();
                //System.out.println(configBuilderFile.toString());
                configBuilderFile.writeTo(processingEnv.getFiler());
            }
        } catch (ProcessorException e) {
            error(e.getMessage(), e.getElement());
        } catch (IOException e) {
            error("Can't generate source file: " + e.getMessage());
        }
        return true;
    }

    private void validateInterface(Element element) {
        if (element.getKind() != ElementKind.INTERFACE) {
            throw new ProcessorException(msg(ONLY_INTERFACE), element);
        }
    }

    private void validateMethodExist(Element element, Set<ExecutableElement> allMethodsElements) {
        if (allMethodsElements.isEmpty()) {
            throw new ProcessorException(msg(METHOD_MUST_EXIST), element);
        }
    }

    private void error(String msg, Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    private void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }
}
