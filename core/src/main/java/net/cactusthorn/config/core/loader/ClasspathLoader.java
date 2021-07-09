package net.cactusthorn.config.core.loader;

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

public abstract class ClasspathLoader implements Loader {

    private final Logger log = Logger.getLogger(getClass().getName());

    protected static final String CLASSPATH_SCHEME = "classpath";

    protected boolean accept(URI uri, String extention) {
        return uri.isOpaque() && CLASSPATH_SCHEME.equals(uri.getScheme()) && uri.getSchemeSpecificPart().endsWith(extention);
    }

    protected abstract Map<String, String> load(Reader reader) throws Exception;

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
        String charsetName = uri.getFragment() == null ? StandardCharsets.UTF_8.name() : uri.getFragment();
        try (InputStream stream = classLoader.getResourceAsStream(uri.getSchemeSpecificPart());
                Reader reader = new InputStreamReader(stream, charsetName);
                BufferedReader buffer = new BufferedReader(reader)) {
            return load(buffer);
        } catch (Exception e) {
            log.info(msg(CANT_LOAD_RESOURCE, uri.toString(), e.toString()));
            return Collections.emptyMap();
        }
    }
}
