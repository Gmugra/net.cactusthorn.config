package net.cactusthorn.config.tests.converter;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Default;
import net.cactusthorn.config.core.converter.ConverterClass;
import net.cactusthorn.config.core.converter.standard.URLConverter;

@Config public interface ConfigConverter {

    @ConverterClass(URLConverter.class) @Default("https://github.com") URL url();

    @ConverterClass(URLConverter.class) Optional<URL> ourl();

    @ConverterClass(URLConverter.class) Optional<List<URL>> list();
}
