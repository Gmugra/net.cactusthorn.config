package net.cactusthorn.config.core.converter.standard;

import java.util.Currency;

import net.cactusthorn.config.core.converter.Converter;

public class CurrencyConverter implements Converter<Currency> {

    @Override public Currency convert(String value, String[] parameters) {
        return Currency.getInstance(value);
    }
}
