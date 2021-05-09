package net.cactusthorn.config.core.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class ApiMessages {

    private static final String BANDLE = ApiMessages.class.getName();
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(BANDLE, Locale.getDefault());

    public enum Key {
        IS_NULL, IS_EMPTY, CANT_LOAD_RESOURCE, VALUE_NOT_FOUND, LOADER_NOT_FOUND, CANT_INVOKE_CONFIGBUILDER, CANT_FIND_CONFIGBUILDER,
        WRONG_SOURCE_PARAM, DURATION_NO_NUMBER, DURATION_WRONG_TIME_UNIT, PERIOD_NO_NUMBER, PERIOD_WRONG_TIME_UNIT
    }

    private ApiMessages() {
    }

    public static String msg(Key key, Object argument) {
        return MessageFormat.format(MESSAGES.getString(key.name()), argument);
    }

    public static String msg(Key key, Object argument1, Object argument2) {
        return MessageFormat.format(MESSAGES.getString(key.name()), argument1, argument2);
    }

    public static String isNull(Object argument) {
        return MessageFormat.format(MESSAGES.getString(Key.IS_NULL.name()), argument);
    }

    public static String isEmpty(Object argument) {
        return MessageFormat.format(MESSAGES.getString(Key.IS_EMPTY.name()), argument);
    }
}
