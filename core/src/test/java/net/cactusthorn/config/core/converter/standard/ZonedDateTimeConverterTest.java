package net.cactusthorn.config.core.converter.standard;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class ZonedDateTimeConverterTest {

    static final ZonedDateTime DATE = ZonedDateTime.of(LocalDateTime.of(2016, 3, 10, 8, 11, 33), ZoneId.systemDefault());
    static final Converter<ZonedDateTime> CONVERTER = new ZonedDateTimeConverter();

    @Test public void simple() {
        CONVERTER.convert("2016-03-10T08:11:33Z");
    }

    @Test public void simpleNullParameters() {
        CONVERTER.convert("2016-03-10T08:11:33Z", null);
    }

    @Test public void simpleEmptyParameters() {
        CONVERTER.convert("2016-03-10T08:11:33Z", new String[0]);
    }

    @Test public void complex() {
        CONVERTER.convert("10.03.2016 08:11:33Z", new String[] { "yyyy-MM-dd'T'HH:mm:sszzz", "dd.MM.yyyy' 'HH:mm:sszzz" });
    }

    @Test public void singleParam() {
        CONVERTER.convert("2016-03-10T08:11:33Z", new String[] { "yyyy-MM-dd'T'HH:mm:sszzz" });
    }
}
