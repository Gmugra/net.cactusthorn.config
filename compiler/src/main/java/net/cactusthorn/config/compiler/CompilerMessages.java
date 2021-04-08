package net.cactusthorn.config.compiler;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class CompilerMessages {

    private static final String BANDLE = CompilerMessages.class.getName();
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(BANDLE, Locale.getDefault());

    public enum Key {
        ONLY_INTERFACE
    }

    private CompilerMessages() {
    }

    public static String msg(Key key) {
        return MESSAGES.getString(key.name());
    }

    public static String msg(Key key, Object argument) {
        return MessageFormat.format(MESSAGES.getString(key.name()), argument);
    }

    public static String msg(Key key, Object argument1, Object arguments2) {
        return MessageFormat.format(MESSAGES.getString(key.name()), argument1, arguments2);
    }

    public static String msg(Key key, Object... arguments) {
        return MessageFormat.format(MESSAGES.getString(key.name()), arguments);
    }
}
