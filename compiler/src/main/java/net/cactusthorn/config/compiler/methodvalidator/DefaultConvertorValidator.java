package net.cactusthorn.config.compiler.methodvalidator;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;
import net.cactusthorn.config.core.converter.URIConverter;
import net.cactusthorn.config.core.converter.URLConverter;
import net.cactusthorn.config.core.converter.DurationConverter;
import net.cactusthorn.config.core.converter.InstantConverter;
import net.cactusthorn.config.core.converter.PathConverter;

public class DefaultConvertorValidator extends MethodValidatorAncestor {

    public static final Map<Class<?>, String> CONVERTERS;
    static {
        CONVERTERS = new HashMap<>();
        CONVERTERS.put(URL.class, URLConverter.class.getName());
        CONVERTERS.put(URI.class, URIConverter.class.getName());
        CONVERTERS.put(Instant.class, InstantConverter.class.getName());
        CONVERTERS.put(Path.class, PathConverter.class.getName());
        CONVERTERS.put(Duration.class, DurationConverter.class.getName());
    }

    private final Map<TypeMirror, Type> classTypes = new HashMap<>();

    public DefaultConvertorValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        for (Class<?> clazz : CONVERTERS.keySet()) {
            classTypes.put(processingEnv().getElementUtils().getTypeElement(clazz.getName()).asType(), clazz);
        }
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        Element element = declaredType.asElement();
        // @formatter:off
        Optional<Type> classType =
            classTypes.entrySet().stream()
            .filter(e -> processingEnv().getTypeUtils().isSameType(element.asType(), e.getKey()))
            .map(e -> e.getValue())
            .findAny();
        // @formatter:off
        if (!classType.isPresent()) {
            return next(methodElement, typeMirror);
        }
        TypeMirror converter = processingEnv().getElementUtils().getTypeElement(CONVERTERS.get(classType.get())).asType();
        return new MethodInfo(methodElement).withConverter(converter);
    }
}
