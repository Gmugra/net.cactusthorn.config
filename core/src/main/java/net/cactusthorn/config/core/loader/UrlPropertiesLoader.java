package net.cactusthorn.config.core.loader;

import static net.cactusthorn.config.core.ApiMessages.msg;
import static net.cactusthorn.config.core.ApiMessages.Key.CANT_LOAD_RESOURCE;

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

public final class UrlPropertiesLoader implements Loader {

    private static final Logger LOG = Logger.getLogger(UrlPropertiesLoader.class.getName());

    private static final String EXTENTION = ".properties";

    @Override public boolean accept(URI uri) {
        if (!uri.getSchemeSpecificPart().endsWith(EXTENTION)) {
            return false;
        }
        try {
            uri.toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
        String charsetName = uri.getFragment() == null ? StandardCharsets.UTF_8.name() : uri.getFragment();
        try (InputStream stream = uri.toURL().openStream();
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
