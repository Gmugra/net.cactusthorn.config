package net.cactusthorn.config.compiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.cactusthorn.config.core.Config;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public final class ConfigProcessor extends AbstractProcessor {

    private static final Set<String> SUPPORTED_ANNOTATIONS = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("net.cactusthorn.config.core.Config")));

    @Override public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Config.class);
            for (Element element : elements) {
                //TODO implementation, next command is placeholder
                System.out.println(element);
            }
        } catch (ProcessorException e) {
            error(e.getMessage(), e.getElement());
        }
        return true;
    }

    private void error(String msg, Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }
}
