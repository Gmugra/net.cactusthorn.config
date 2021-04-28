package test;

import java.net.URL;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.converter.ConverterClass;
import net.cactusthorn.config.core.converter.URLConverter;

@Config public interface ConfigConverter {

    @ConverterClass(URLConverter.class) URL url();
}
