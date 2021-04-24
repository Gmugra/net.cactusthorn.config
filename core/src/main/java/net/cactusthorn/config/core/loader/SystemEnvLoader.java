package net.cactusthorn.config.core.loader;

import java.net.URI;
import java.util.Map;

public class SystemEnvLoader implements Loader {

    private static final String SCHEME = "system";
    private static final String PART = "env";

    @Override public boolean accept(URI uri) {
        return uri.isOpaque() && SCHEME.equals(uri.getScheme()) && uri.getSchemeSpecificPart().equals(PART);
    }

    public Map<String, String> load(URI uri, ClassLoader classLoader) {
        return System.getenv();
    }

}
