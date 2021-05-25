package test;

import java.time.Instant;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.List;

import net.cactusthorn.config.core.Config;

@Config public interface ConfigDefaultConverters {
    Instant instant();

    Optional<URL> url();

    List<URI> uri();
}
