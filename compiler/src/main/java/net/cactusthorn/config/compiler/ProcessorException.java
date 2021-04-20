package net.cactusthorn.config.compiler;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public final class ProcessorException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    private final Element element;
    private final AnnotationMirror annotationMirror;

    public ProcessorException(String message, Element element) {
        super(message);
        this.element = element;
        this.annotationMirror = null;
    }

    public ProcessorException(String message, Element element, AnnotationMirror annotationMirror) {
        super(message);
        this.element = element;
        this.annotationMirror = annotationMirror;
    }

    public Element getElement() {
        return element;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }
}
