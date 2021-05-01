package net.cactusthorn.config.core.loader;

import static net.cactusthorn.config.core.ApiMessages.msg;
import static net.cactusthorn.config.core.ApiMessages.Key.LOADER_NOT_FOUND;

import java.net.URI;
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
            this.uri = uri;
            this.cachable = cachable;
        }

        public UriTemplate(String template, boolean cachable) {
            this.template = template;
            this.cachable = cachable;
            if (template.indexOf("{") != -1) {
                variable = true;
            } else {
                uri = URI.create(template);
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" }) private URI uri() {
            if (!variable) {
                return uri;
            }
            Map<String, String> values = new HashMap<>(System.getenv());
            values.putAll((Map) System.getProperties());
            return URI.create(new VariablesParser(template).replace(values));
        }

        private boolean cachable() {
            return cachable;
        }
    }

    private final ConcurrentHashMap<URI, Map<String, String>> cache = new ConcurrentHashMap<>();

    private final LoadStrategy loadStrategy;
    private final LinkedHashSet<UriTemplate> templates;
    private final Deque<Loader> loaders;

    public Loaders(LoadStrategy loadStrategy, LinkedHashSet<UriTemplate> templates, Deque<Loader> loaders) {
        this.loadStrategy = loadStrategy;
        this.templates = templates;
        this.loaders = loaders;
    }

    public Map<String, String> load(ClassLoader classLoader) {
        List<Map<String, String>> values = new ArrayList<>();
        for (UriTemplate template : templates) {
            URI uri = template.uri();
            Loader loader = loaders.stream().filter(l -> l.accept(uri)).findFirst()
                    .orElseThrow(() -> new UnsupportedOperationException(msg(LOADER_NOT_FOUND, uri)));
            Map<String, String> properties;
            if (template.cachable()) {
                properties = cache.computeIfAbsent(uri, u -> loader.load(u, classLoader));
            } else {
                properties = loader.load(uri, classLoader);
            }
            values.add(properties);
        }
        return loadStrategy.combine(values);
    }
}
