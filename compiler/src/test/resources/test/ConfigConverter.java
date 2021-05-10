package test;

import java.io.Serializable;
import java.time.Duration;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.converter.ConverterClass;
import net.cactusthorn.config.core.converter.standard.DurationConverter;

@Config public interface ConfigConverter extends Serializable {

    String serialVersionUID = "A"; // to test that this line will be ignored becasue of wrong type

    @ConverterClass(DurationConverter.class) Duration duration();
}
