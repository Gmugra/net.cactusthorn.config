package net.cactusthorn.config.core.loader;

import java.net.URI;
import java.util.Map;

public interface Loader {

     /**
     * Indicates whether this Loader does accept the URI.
     *
     * @param uri  the URI as String
     * @return URI, if the loader is able to handle the content of the URI.
     */
    boolean accept(URI uri);

     /**
     * Loads the given {@link URI uri}
     *
     * @param uri the {@link URI} from where to load the properties.
     * @return immutable Map, can't be null.
     */
    Map<String, String> load(URI uri, ClassLoader classLoader);
}
