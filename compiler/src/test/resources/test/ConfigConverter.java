package test;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.converter.ConverterClass;
import net.cactusthorn.config.core.converter.LocalDateParser;
import net.cactusthorn.config.core.converter.standard.DurationConverter;

@Config public interface ConfigConverter extends Serializable {

    String serialVersionUID = "A"; // to test that this line will be ignored becasue of wrong type

    @ConverterClass(DurationConverter.class) Duration duration();

    @LocalDateParser LocalDate localDate();

    @LocalDateParser({"dd.MM.yyyy", "yyyy-MM-dd"}) LocalDate localDate2();

    final class ToTestConverter implements Converter<String> {

        @Override public String convert(String value, String[] parameters) {
            return "test::" + value;
        }
    }

    @ConverterClass(ToTestConverter.class) String testConverter();
}
