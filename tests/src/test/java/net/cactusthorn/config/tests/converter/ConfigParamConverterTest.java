package net.cactusthorn.config.tests.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class ConfigParamConverterTest {

    @Test public void convert() {
        Map<String, String> properties = new HashMap<>();
        properties.put("localDate", "12.11.2011");
        ConfigParamConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(LocalDate.of(2011, 11, 12), config.localDate());
    }

    @Test public void convertDT() {
        Map<String, String> properties = new HashMap<>();
        properties.put("localDate", "12.11.2011");
        properties.put("localDateTime", "10.03.2016 08:11:33");
        ConfigParamConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(LocalDateTime.of(2016, 3, 10, 8, 11, 33), config.localDateTime().get());
    }

    @Test public void convertZDT() {
        Map<String, String> properties = new HashMap<>();
        properties.put("localDate", "12.11.2011");
        properties.put("zonedDateTime", "10.03.2016 08:11:33Z");
        ConfigParamConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertTrue(config.zonedDateTime().isPresent());
    }
}
