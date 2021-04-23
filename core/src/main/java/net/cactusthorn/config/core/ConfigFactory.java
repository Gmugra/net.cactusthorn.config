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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.cactusthorn.config.core.loader.ClasspathPropertiesLoader;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loader;

public final class ConfigFactory {

    private static final MethodType CONSTRUCTOR = MethodType.methodType(void.class, Map.class);
    private static final ConcurrentHashMap<Class<?>, MethodHandle> BUILDERS = new ConcurrentHashMap<>();

    private final LoadStrategy loadStrategy;
    private final Map<String, String> props;
    private final LinkedHashMap<URI, Loader> uriLoaders;

    private ConfigFactory(LoadStrategy loadStrategy, Map<String, String> properties, LinkedHashMap<URI, Loader> uriLoaders) {
        this.loadStrategy = loadStrategy;
        this.props = properties;
        this.uriLoaders = uriLoaders;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final List<Loader> loaders = new ArrayList<>();
        private final LinkedHashSet<URI> uris = new LinkedHashSet<>();

        private Map<String, String> props = Collections.emptyMap();
        private LoadStrategy loadStrategy = LoadStrategy.MERGE;

        private Builder() {
            loaders.add(new ClasspathPropertiesLoader());
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
            return addSource(Function.identity(), uri);
        }

        public Builder addSource(String... uri) {
            return addSource(URI::create, uri);
        }

        @SuppressWarnings("unchecked") private <T> Builder addSource(Function<T, URI> convert, T... uri) {
            if (uri == null) {
                throw new IllegalArgumentException(isNull("uri"));
            }
            if (uri.length == 0) {
                throw new IllegalArgumentException(isEmpty("uri"));
            }
            for (T u : uri) {
                if (u != null) {
                    uris.add(convert.apply(u));
                }
            }
            return this;
        }

        public ConfigFactory build() {
            LinkedHashMap<URI, Loader> uriLoaders = new LinkedHashMap<>();
            for (URI uri : uris) {
                Loader loader = loaders.stream().filter(l -> l.accept(uri)).findAny()
                        .orElseThrow(() -> new UnsupportedOperationException(msg(LOADER_NOT_FOUND, uri)));
                uriLoaders.put(uri, loader);
            }
            return new ConfigFactory(loadStrategy, props, uriLoaders);
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
        //TODO caching for properties by URI
        // @formatter:off
        return loadStrategy.combine(
            uriLoaders.entrySet().stream()
                .map(e -> e.getValue().load(e.getKey(), sourceInterface.getClassLoader()))
                .collect(Collectors.toList()));
        // @formatter:on
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
