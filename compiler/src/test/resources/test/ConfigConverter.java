package test;

import java.time.Duration;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.converter.ConverterClass;
import net.cactusthorn.config.core.converter.DurationConverter;

@Config public interface ConfigConverter {

    @ConverterClass(DurationConverter.class) Duration duration();
}
