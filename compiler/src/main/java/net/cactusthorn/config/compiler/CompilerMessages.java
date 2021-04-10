package net.cactusthorn.config.compiler;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class CompilerMessages {

    private static final String BANDLE = CompilerMessages.class.getName();
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(BANDLE, Locale.getDefault());

    public enum Key {
        ONLY_INTERFACE,
        METHOD_MUST_EXIST,
        METHOD_WITHOUT_PARAMETERS,
        RETURN_VOID,
        RETURN_INTERFACES,
        RETURN_ABSTRACT,
        RETURN_INTERFACE_ARG_EMPTY,
        RETURN_INTERFACE_ARG_WILDCARD,
        RETURN_INTERFACE_ARG_INTERFACE,
        RETURN_STRING_CLASS,
        RETURN_OPTIONAL_ARG_EMPTY,
        RETURN_OPTIONAL_ARG_WILDCARD
    }

    private CompilerMessages() {
    }

    public static String msg(Key key) {
        return MESSAGES.getString(key.name());
    }

    public static String msg(Key key, Object argument) {
        return MessageFormat.format(MESSAGES.getString(key.name()), argument);
    }
}
