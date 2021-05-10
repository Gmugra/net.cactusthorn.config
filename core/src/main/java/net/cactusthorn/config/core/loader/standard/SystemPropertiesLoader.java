package net.cactusthorn.config.core.loader.standard;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import net.cactusthorn.config.core.loader.Loader;

public final class SystemPropertiesLoader implements Loader {

    private static final String SCHEME = "system";
    private static final String PART = "properties";

    @Override public boolean accept(URI uri) {
        return uri.isOpaque() && SCHEME.equals(uri.getScheme()) && uri.getSchemeSpecificPart().equals(PART);
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" }) public Map<String, String> load(URI uri, ClassLoader classLoader) {
        return new HashMap<>((Map) System.getProperties());
    }

}
