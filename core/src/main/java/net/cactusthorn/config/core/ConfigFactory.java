/*
* Copyright (C) 2021, Alexei Khatskevich
*
* Licensed under the BSD 3-Clause license.
* You may obtain a copy of the License at
*
* https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.util.ConfigInitializer;

public final class ConfigFactory {

    private static final MethodType CONFIG_CONSTRUCTOR = MethodType.methodType(void.class, Loaders.class);
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
            ServiceLoader<Loader> serviceLoader = ServiceLoader.load(Loader.class);
            for (Iterator<Loader> it = serviceLoader.iterator(); it.hasNext();) {
                loaders.add(it.next());
            }
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
            return addSources(u -> new UriTemplate(u), uri);
        }

        public Builder addSource(String... uri) {
            return addSources(u -> new UriTemplate(u), uri);
        }

        @SuppressWarnings({"unchecked"}) private <T> Builder addSources(Function<T, UriTemplate> mapper, T... uri) {
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

    public <T> T create(Class<T> sourceInterface) {
        try {
            MethodHandle methodHandler = BUILDERS.computeIfAbsent(sourceInterface, this::findConfigConstructor);
            return (T) methodHandler.invoke(loaders);
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

    private <T> MethodHandle findConfigConstructor(Class<T> sourceInterface) {
        Package interfacePackage = sourceInterface.getPackage();
        String interfaceName = sourceInterface.getSimpleName();
        String builderClassName = interfacePackage.getName() + '.' + ConfigInitializer.CONFIG_CLASSNAME_PREFIX + interfaceName;
        try {
            Class<?> builderClass = Class.forName(builderClassName);
            return MethodHandles.publicLookup().findConstructor(builderClass, CONFIG_CONSTRUCTOR);
        } catch (Throwable e) {
            throw new IllegalArgumentException(msg(CANT_FIND_CONFIGBUILDER, sourceInterface.getName()), e);
        }
    }
}
