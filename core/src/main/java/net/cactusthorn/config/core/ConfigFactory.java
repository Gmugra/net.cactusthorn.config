package net.cactusthorn.config.core;

import static net.cactusthorn.config.core.ApiMessages.*;
import static net.cactusthorn.config.core.ApiMessages.Key.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.cactusthorn.config.core.loader.ClasspathPropertiesLoader;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.core.loader.SystemEnvLoader;
import net.cactusthorn.config.core.loader.SystemPropertiesLoader;
import net.cactusthorn.config.core.loader.UrlPropertiesLoader;
import net.cactusthorn.config.core.util.VariablesParser;

public final class ConfigFactory {

    private static final class UriTemplate {
        private URI uri;
        private String template;
        private boolean variable = false;
        private boolean cachable = true;

        private UriTemplate(URI uri, boolean cachable) {
            this.uri = uri;
            this.cachable = cachable;
        }

        private UriTemplate(String template, boolean cachable) {
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

    private static final MethodType CONSTRUCTOR = MethodType.methodType(void.class, ConfigHolder.class);
    private static final ConcurrentHashMap<Class<?>, MethodHandle> BUILDERS = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<URI, Map<String, String>> cache = new ConcurrentHashMap<>();

    private final LoadStrategy loadStrategy;
    private final Map<String, String> props;
    private final LinkedHashSet<UriTemplate> templates;
    private final Deque<Loader> loaders;

    private ConfigFactory(LoadStrategy loadStrategy, Map<String, String> properties, LinkedHashSet<UriTemplate> templates,
            Deque<Loader> loaders) {
        this.loadStrategy = loadStrategy;
        this.props = properties;
        this.templates = templates;
        this.loaders = loaders;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static final MethodType DEFAULT_CONSTRUCTOR = MethodType.methodType(void.class);

        private final ArrayDeque<Loader> loaders = new ArrayDeque<>();
        private final LinkedHashSet<UriTemplate> templates = new LinkedHashSet<>();

        private Map<String, String> props = Collections.emptyMap();
        private LoadStrategy loadStrategy = LoadStrategy.MERGE;

        private Builder() {
            loaders.add(new ClasspathPropertiesLoader());
            loaders.add(new SystemPropertiesLoader());
            loaders.add(new SystemEnvLoader());
            loaders.add(new UrlPropertiesLoader());
        }

        public Builder addLoader(Loader loader) {
            if (loader == null) {
                throw new IllegalArgumentException(isNull("loader"));
            }
            loaders.addFirst(loader);
            return this;
        }

        public Builder addLoader(Class<? extends Loader> loaderClass) {
            if (loaderClass == null) {
                throw new IllegalArgumentException(isNull("loaderClass"));
            }
            try {
                MethodHandle methodHandle = MethodHandles.publicLookup().findConstructor(loaderClass, DEFAULT_CONSTRUCTOR);
                return addLoader((Loader) methodHandle.invoke());
            } catch (Throwable e) {
                throw new IllegalArgumentException(e);
            }
        }

        public Builder setLoadStrategy(LoadStrategy strategy) {
            if (strategy == null) {
                throw new IllegalArgumentException(isNull("strategy"));
            }
            loadStrategy = strategy;
            return this;
        }

        public Builder setSource(Map<String, String> properties) {
            if (properties == null) {
                throw new IllegalArgumentException(isNull("properties"));
            }
            props = properties;
            return this;
        }

        public Builder addSource(URI... uri) {
            return addSources(u -> new UriTemplate(u, true), uri);
        }

        public Builder addSourceNoCache(URI... uri) {
            return addSources(u -> new UriTemplate(u, false), uri);
        }

        public Builder addSource(String... uri) {
            return addSources(u -> new UriTemplate(u, true), uri);
        }

        public Builder addSourceNoCache(String... uri) {
            return addSources(u -> new UriTemplate(u, false), uri);
        }

        @SuppressWarnings({ "unchecked" }) private <T> Builder addSources(Function<T, UriTemplate> mapper, T... uri) {
            if (uri == null) {
                throw new IllegalArgumentException(isNull("uri"));
            }
            if (uri.length == 0) {
                throw new IllegalArgumentException(isEmpty("uri"));
            }
            templates.addAll(Stream.of(uri).filter(u -> u != null).map(u -> mapper.apply(u)).collect(Collectors.toList()));
            return this;
        }

        public ConfigFactory build() {
            return new ConfigFactory(loadStrategy, props, templates, loaders);
        }
    }

    @SuppressWarnings("unchecked") public <T> T create(Class<T> sourceInterface) {
        ConfigHolder configHolder = configHolder(sourceInterface.getClassLoader());
        try {
            MethodHandle methodHandler = BUILDERS.computeIfAbsent(sourceInterface, this::findBuilderConstructor);
            @SuppressWarnings("rawtypes") ConfigBuilder builder = (ConfigBuilder) methodHandler.invoke(configHolder);
            return (T) builder.build();
        } catch (Throwable e) {
            throw new IllegalArgumentException(msg(CANT_INVOKE_CONFIGBUILDER, sourceInterface.getName()), e);
        }
    }

    public ConfigHolder configHolder(ClassLoader classLoader) {
        Map<String, String> forBuilder = new HashMap<>();
        forBuilder.putAll(load(classLoader));
        forBuilder.putAll(props); // Map with properties is always has highest priority
        return new ConfigHolder(forBuilder);
    }

    public ConfigHolder configHolder() {
        return configHolder(ConfigFactory.class.getClassLoader());
    }

    private Map<String, String> load(ClassLoader classLoader) {
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

    private <T> MethodHandle findBuilderConstructor(Class<T> sourceInterface) {
        Package interfacePackage = sourceInterface.getPackage();
        String interfaceName = sourceInterface.getSimpleName();
        String builderClassName = interfacePackage.getName() + '.' + ConfigBuilder.BUILDER_CLASSNAME_PREFIX + interfaceName;
        try {
            Class<?> builderClass = Class.forName(builderClassName);
            return MethodHandles.publicLookup().findConstructor(builderClass, CONSTRUCTOR);
        } catch (Throwable e) {
            throw new IllegalArgumentException(msg(CANT_FIND_CONFIGBUILDER, sourceInterface.getName()), e);
        }
    }
}
