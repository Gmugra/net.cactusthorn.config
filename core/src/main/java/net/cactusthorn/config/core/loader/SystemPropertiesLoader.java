package net.cactusthorn.config.core.loader;

import java.net.URI;
import java.util.Map;

public class SystemPropertiesLoader implements Loader {

    private static final String SCHEME = "system";
    private static final String PART = "properties";

    @Override public boolean accept(URI uri) {
        return uri.isOpaque() && SCHEME.equals(uri.getScheme()) && uri.getSchemeSpecificPart().equals(PART);
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" }) public Map<String, String> load(URI uri, ClassLoader classLoader) {
        return (Map) System.getProperties();
    }

}
