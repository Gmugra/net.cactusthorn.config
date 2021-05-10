package net.cactusthorn.config.core.loader.standard;

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.CANT_LOAD_RESOURCE;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.core.util.XMLToMapParser;

public class ClasspathXMLLoader implements Loader {

    private static final Logger LOG = Logger.getLogger(ClasspathXMLLoader.class.getName());

    private static final String SCHEME = "classpath";
    private static final String EXTENTION = ".xml";

    @Override public boolean accept(URI uri) {
        return uri.isOpaque() && SCHEME.equals(uri.getScheme()) && uri.getSchemeSpecificPart().endsWith(EXTENTION);
    }

    private static final XMLToMapParser PARSER = new XMLToMapParser();

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
        String charsetName = uri.getFragment() == null ? StandardCharsets.UTF_8.name() : uri.getFragment();
        try (InputStream stream = classLoader.getResourceAsStream(uri.getSchemeSpecificPart());
                Reader reader = new InputStreamReader(stream, charsetName);
                BufferedReader buffer = new BufferedReader(reader)) {
            return PARSER.parse(buffer);
        } catch (Exception e) {
            LOG.info(msg(CANT_LOAD_RESOURCE, uri.toString(), e.toString()));
            return Collections.emptyMap();
        }
    }
}
