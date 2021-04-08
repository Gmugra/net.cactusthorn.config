package net.cactusthorn.config.compiler;

import javax.lang.model.element.Element;

final class ProcessorException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    private final Element element;

    ProcessorException(String message, Element element) {
        super(message);
        this.element = element;
    }

    Element getElement() {
        return element;
    }
}
