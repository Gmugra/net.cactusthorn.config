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
package net.cactusthorn.config.core.factory;

import static net.cactusthorn.config.core.util.ApiMessages.isEmpty;
import static net.cactusthorn.config.core.util.ApiMessages.isNull;

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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.loader.Loaders.UriTemplate;

public abstract class ConfigFactoryBuilder {

    private static final MethodType DEFAULT_CONSTRUCTOR = MethodType.methodType(void.class);

    private final ArrayDeque<Loader> loaders = new ArrayDeque<>();
    private final LinkedHashSet<UriTemplate> templates = new LinkedHashSet<>();

    private Map<String, String> props = Collections.emptyMap();
    private LoadStrategy loadStrategy = LoadStrategy.MERGE;
    private long autoReloadPeriodInSeconds = 0L;
    private String globalPrefix = null;

    protected ConfigFactoryBuilder() {
        ServiceLoader<Loader> serviceLoader = ServiceLoader.load(Loader.class);
        for (Iterator<Loader> it = serviceLoader.iterator(); it.hasNext();) {
            loaders.add(it.next());
        }
    }

    public ConfigFactoryBuilder addLoader(Loader loader) {
        if (loader == null) {
            throw new IllegalArgumentException(isNull("loader"));
        }
        loaders.addFirst(loader);
        return this;
    }

    public ConfigFactoryBuilder addLoader(Class<? extends Loader> loaderClass) {
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

    public ConfigFactoryBuilder setLoadStrategy(LoadStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException(isNull("strategy"));
        }
        loadStrategy = strategy;
        return this;
    }

    public ConfigFactoryBuilder setSource(Map<String, String> properties) {
        if (properties == null) {
            throw new IllegalArgumentException(isNull("properties"));
        }
        props = properties;
        return this;
    }

    public ConfigFactoryBuilder addSource(URI... uri) {
        return addSources(u -> new UriTemplate(u), uri);
    }

    public ConfigFactoryBuilder addSource(String... uri) {
        return addSources(u -> new UriTemplate(u), uri);
    }

    public ConfigFactoryBuilder autoReload(long periodInSeconds) {
        this.autoReloadPeriodInSeconds = periodInSeconds;
        return this;
    }

    public ConfigFactoryBuilder setGlobalPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException(isNull("prefix"));
        }
        if (prefix.length() == 0) {
            throw new IllegalArgumentException(isEmpty("prefix"));
        }
        this.globalPrefix = prefix;
        return this;
    }

    @SuppressWarnings({"unchecked"}) private <T> ConfigFactoryBuilder addSources(Function<T, UriTemplate> mapper, T... uri) {
        if (uri == null) {
            throw new IllegalArgumentException(isNull("uri"));
        }
        if (uri.length == 0) {
            throw new IllegalArgumentException(isEmpty("uri"));
        }
        templates.addAll(Stream.of(uri).filter(u -> u != null).map(u -> mapper.apply(u)).collect(Collectors.toList()));
        return this;
    }

    protected Loaders createLoaders() {
        return new Loaders(loadStrategy, templates, loaders, props, autoReloadPeriodInSeconds, globalPrefix);
    }

    public abstract ConfigFactoryAncestor build();
}
