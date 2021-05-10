package net.cactusthorn.config.core;

import static net.cactusthorn.config.core.loader.Loaders.UriTemplate;
import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.cactusthorn.config.core.loader.ClasspathJarManifestLoader;
import net.cactusthorn.config.core.loader.ClasspathPropertiesLoader;
import net.cactusthorn.config.core.loader.ClasspathXMLLoader;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.loader.SystemEnvLoader;
import net.cactusthorn.config.core.loader.SystemPropertiesLoader;
import net.cactusthorn.config.core.loader.UrlPropertiesLoader;
import net.cactusthorn.config.core.loader.UrlXMLLoader;

public final class ConfigFactory {

    private static final MethodType BUILDER_CONSTRUCTOR = MethodType.methodType(void.class, Loaders.class);
    private static final ConcurrentHashMap<Class<?>, MethodHandle> BUILDERS = new ConcurrentHashMap<>();

    private final Loaders loaders;

    private ConfigFactory(Loaders loaders) {
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
            loaders.add(new ClasspathXMLLoader());
            loaders.add(new UrlXMLLoader());
            loaders.add(new ClasspathJarManifestLoader());
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
            Loaders allLoaders = new Loaders(loadStrategy, templates, loaders, props);
            return new ConfigFactory(allLoaders);
        }
    }

    @SuppressWarnings("unchecked") public <T> T create(Class<T> sourceInterface) {
        try {
            MethodHandle methodHandler = BUILDERS.computeIfAbsent(sourceInterface, this::findBuilderConstructor);
            @SuppressWarnings("rawtypes") ConfigBuilder builder = (ConfigBuilder) methodHandler.invoke(loaders);
            return (T) builder.build();
        } catch (Throwable e) {
            throw new IllegalArgumentException(msg(CANT_INVOKE_CONFIGBUILDER, sourceInterface.getName()), e);
        }
    }

    public ConfigHolder configHolder(ClassLoader classLoader) {
        return loaders.load(classLoader);
    }

    public ConfigHolder configHolder() {
        return configHolder(ConfigFactory.class.getClassLoader());
    }

    private <T> MethodHandle findBuilderConstructor(Class<T> sourceInterface) {
        Package interfacePackage = sourceInterface.getPackage();
        String interfaceName = sourceInterface.getSimpleName();
        String builderClassName = interfacePackage.getName() + '.' + ConfigBuilder.BUILDER_CLASSNAME_PREFIX + interfaceName;
        try {
            Class<?> builderClass = Class.forName(builderClassName);
            return MethodHandles.publicLookup().findConstructor(builderClass, BUILDER_CONSTRUCTOR);
        } catch (Throwable e) {
            throw new IllegalArgumentException(msg(CANT_FIND_CONFIGBUILDER, sourceInterface.getName()), e);
        }
    }
}
