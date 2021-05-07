package net.cactusthorn.config.core.loader;

import static net.cactusthorn.config.core.ApiMessages.msg;
import static net.cactusthorn.config.core.ApiMessages.Key.LOADER_NOT_FOUND;

import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.cactusthorn.config.core.util.VariablesParser;

public final class Loaders {

    public static final class UriTemplate {
        private URI uri;
        private String template;
        private boolean variable = false;
        private boolean cachable = true;

        public UriTemplate(URI uri, boolean cachable) {
            this.uri = replace(uri, cachable);
        }

        public UriTemplate(String template, boolean cachable) {
            this.template = replace(template, cachable);
            if (template.indexOf("{") != -1) {
                variable = true;
            } else {
                uri = URI.create(this.template);
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" }) URI uri() {
            if (!variable) {
                return uri;
            }
            Map<String, String> values = new HashMap<>(System.getenv());
            values.putAll((Map) System.getProperties());
            return URI.create(new VariablesParser(template).replace(values));
        }

        boolean cachable() {
            return cachable;
        }

        private static final String USERHOME_PREFIX = "file:~/";

        private URI replace(URI u, boolean cache) {
            String tmp = replace(u.toString(), cache);
            return URI.create(tmp);
        }

        private static final String NOCACHE = "nocache:";
        private static final String USER_HOME = "user.home";

        private String replace(String str, boolean cache) {
            String result = str;
            if (result.indexOf(NOCACHE) == 0) {
                this.cachable = false;
                result = result.substring(NOCACHE.length());
            } else {
                this.cachable = cache;
            }
            if (result.indexOf(USERHOME_PREFIX) == -1) {
                return result;
            }
            String userHome = Paths.get(System.getProperty(USER_HOME)).toUri().toString();
            return result.replace(USERHOME_PREFIX, userHome);
        }
    }

    private final ConcurrentHashMap<URI, Map<String, String>> cache = new ConcurrentHashMap<>();

    private final LoadStrategy loadStrategy;
    private final LinkedHashSet<UriTemplate> templates;
    private final Deque<Loader> loaders;
    private final Map<String, String> properties;

    public Loaders(LoadStrategy loadStrategy, LinkedHashSet<UriTemplate> templates, Deque<Loader> loaders, Map<String, String> properties) {
        this.loadStrategy = loadStrategy;
        this.templates = templates;
        this.loaders = loaders;
        this.properties = properties;
    }

    public Map<String, String> load(ClassLoader classLoader) {
        List<Map<String, String>> values = new ArrayList<>();
        for (UriTemplate template : templates) {
            URI uri = template.uri();
            Loader loader = loaders.stream().filter(l -> l.accept(uri)).findFirst()
                    .orElseThrow(() -> new UnsupportedOperationException(msg(LOADER_NOT_FOUND, uri)));
            Map<String, String> uriProperties;
            if (template.cachable()) {
                uriProperties = cache.computeIfAbsent(uri, u -> loader.load(u, classLoader));
            } else {
                uriProperties = loader.load(uri, classLoader);
            }
            values.add(uriProperties);
        }
        return loadStrategy.combine(values, properties);
    }
}