package net.cactusthorn.config.core;

import static net.cactusthorn.config.core.ApiMessages.*;
import static net.cactusthorn.config.core.ApiMessages.Key.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        private UriTemplate(URI uri) {
            this.uri = uri;
        }

        private UriTemplate(String template) {
            this.template = template;
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
    }

    private static final MethodType CONSTRUCTOR = MethodType.methodType(void.class, Map.class);
    private static final ConcurrentHashMap<Class<?>, MethodHandle> BUILDERS = new ConcurrentHashMap<>();

    private final LoadStrategy loadStrategy;
    private final Map<String, String> props;
    private final LinkedHashSet<UriTemplate> templates;
    private final Set<Loader> loaders;

    private ConfigFactory(LoadStrategy loadStrategy, Map<String, String> properties, LinkedHashSet<UriTemplate> templates,
            Set<Loader> loaders) {
        this.loadStrategy = loadStrategy;
        this.props = properties;
        this.templates = templates;
        this.loaders = loaders;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Set<Loader> loaders = new HashSet<>();
        private final LinkedHashSet<UriTemplate> templates = new LinkedHashSet<>();

        private Map<String, String> props = Collections.emptyMap();
        private LoadStrategy loadStrategy = LoadStrategy.MERGE;

        private Builder() {
            loaders.add(new ClasspathPropertiesLoader());
            loaders.add(new SystemPropertiesLoader());
            loaders.add(new SystemEnvLoader());
            loaders.add(new UrlPropertiesLoader());
        }

        public Builder setLoadStrategy(LoadStrategy strategy) {
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
            return addSources(u -> new UriTemplate(u), uri);
        }

        public Builder addSource(String... uri) {
            return addSources(u -> new UriTemplate(u), uri);
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
        Map<String, String> forBuilder = new HashMap<>();
        forBuilder.putAll(load(sourceInterface));
        forBuilder.putAll(props); // Map with properties is always has highest priority
        MethodHandle methodHandler = BUILDERS.computeIfAbsent(sourceInterface, this::findBuilderConstructor);
        try {
            @SuppressWarnings("rawtypes") ConfigBuilder builder = (ConfigBuilder) methodHandler.invoke(forBuilder);
            return (T) builder.build();
        } catch (Throwable e) {
            throw new IllegalArgumentException(msg(CANT_INVOKE_CONFIGBUILDER, sourceInterface.getName()), e);
        }
    }

    public static <T> T create(Class<T> sourceInterface, Map<String, String> properties) {
        return ConfigFactory.builder().setSource(properties).build().create(sourceInterface);
    }

    public static <T> T create(Class<T> sourceInterface, URI... uri) {
        return ConfigFactory.builder().addSource(uri).build().create(sourceInterface);
    }

    public static <T> T create(Class<T> sourceInterface, String... uri) {
        return ConfigFactory.builder().addSource(uri).build().create(sourceInterface);
    }

    private <T> Map<String, String> load(Class<T> sourceInterface) {
        if (sourceInterface == null) {
            throw new IllegalArgumentException(isNull(sourceInterface));
        }
        // TODO caching for properties by URI
        List<Map<String, String>> values = new ArrayList<>();
        for (UriTemplate template : templates) {
            URI uri = template.uri();
            Loader load = loaders.stream().filter(l -> l.accept(uri)).findFirst()
                    .orElseThrow(() -> new UnsupportedOperationException(msg(LOADER_NOT_FOUND, uri)));
            values.add(load.load(uri, sourceInterface.getClassLoader()));
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
