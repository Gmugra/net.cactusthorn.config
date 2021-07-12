package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Currency;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class CurrencyConverterTest {

    private static final Converter<Currency> CONVERTER = new CurrencyConverter();

    @Test public void correct() {
        assertEquals(Currency.getInstance(Locale.GERMANY), CONVERTER.convert("EUR"));
    }
}
