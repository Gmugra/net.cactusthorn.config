package net.cactusthorn.config.core.loader.standard;

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import net.cactusthorn.config.core.loader.Loader;

public final class ClasspathPropertiesLoader implements Loader {

    private static final Logger LOG = Logger.getLogger(ClasspathPropertiesLoader.class.getName());

    private static final String SCHEME = "classpath";
    private static final String EXTENTION = ".properties";

    @Override public boolean accept(URI uri) {
        return uri.isOpaque() && SCHEME.equals(uri.getScheme()) && uri.getSchemeSpecificPart().endsWith(EXTENTION);
    }

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
        String charsetName = uri.getFragment() == null ? StandardCharsets.UTF_8.name() : uri.getFragment();
        try (InputStream stream = classLoader.getResourceAsStream(uri.getSchemeSpecificPart());
                Reader reader = new InputStreamReader(stream, charsetName);
                BufferedReader buffer = new BufferedReader(reader)) {
            Properties properties = new Properties();
            properties.load(buffer);
            @SuppressWarnings({ "unchecked", "rawtypes" }) Map<String, String> result = (Map) properties;
            return result;
        } catch (Exception e) {
            LOG.info(msg(CANT_LOAD_RESOURCE, uri.toString(), e.toString()));
            return Collections.emptyMap();
        }
    }
}
