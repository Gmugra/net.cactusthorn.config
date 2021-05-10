package test;

import java.time.Instant;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.List;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.converter.ConverterClass;
import net.cactusthorn.config.core.converter.standard.DurationConverter;

@Config public interface ConfigDefaultConverters {
    Instant instant();

    Optional<URL> url();

    List<URI> uri();
}
