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
package net.cactusthorn.config.core.loader;

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import net.cactusthorn.config.core.Reloadable;
import net.cactusthorn.config.core.util.VariablesParser;

public final class Loaders {

    public static final class UriTemplate {
        private URI uri;
        private String template;
        private boolean variable = false;
        private boolean cachable = true;

        public UriTemplate(URI uri) {
            this.uri = replace(uri);
        }

        public UriTemplate(String template) {
            this.template = replace(template);
            if (template.indexOf("{") != -1) {
                variable = true;
            } else {
                uri = URI.create(this.template);
            }
        }

        @SuppressWarnings({"rawtypes", "unchecked"}) public URI uri() {
            if (!variable) {
                return uri;
            }
            var values = new HashMap<>(System.getenv());
            values.putAll((Map) System.getProperties());
            return URI.create(new VariablesParser(template).replace(values));
        }

        boolean cachable() {
            return cachable;
        }

        private static final String USERHOME_PREFIX = "file:~/";

        private URI replace(URI u) {
            var tmp = replace(u.toString());
            return URI.create(tmp);
        }

        private static final String NOCACHE = "nocache:";
        private static final String USER_HOME = "user.home";

        private String replace(String str) {
            var result = str;
            if (result.indexOf(NOCACHE) == 0) {
                this.cachable = false;
                result = result.substring(NOCACHE.length());
            }
            if (result.indexOf(USERHOME_PREFIX) == -1) {
                return result;
            }
            var userHome = Paths.get(System.getProperty(USER_HOME)).toUri().toString();
            return result.replace(USERHOME_PREFIX, userHome);
        }
    }

    private final AutoReloader reloader;

    private final ConcurrentHashMap<URI, Map<String, String>> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<URI, Long> hashCodes = new ConcurrentHashMap<>();

    private final LoadStrategy loadStrategy;
    private final LinkedHashSet<UriTemplate> templates;
    private final Deque<Loader> loaders;
    private final Map<String, String> properties;
    private final String globalPrefix;

    public Loaders(LoadStrategy loadStrategy, LinkedHashSet<UriTemplate> templates, Deque<Loader> loaders, Map<String, String> properties,
            long hotReloadPeriodInSeconds, String globalPrefix) {
        this.loadStrategy = loadStrategy;
        this.templates = templates;
        this.loaders = loaders;
        this.properties = properties;
        this.reloader = new AutoReloader(hotReloadPeriodInSeconds);
        this.globalPrefix = globalPrefix;
    }

    public void register(Reloadable reloadable) {
        // TODO debug log
        reloader.register(reloadable);
    }

    public String globalPrefix() {
        return globalPrefix;
    }

    public ConfigHolder load(ClassLoader classLoader) {
        return load(classLoader, loadStrategy, templates);
    }

    public ConfigHolder load(ClassLoader classLoader, LoadStrategy strategy, String[] uris) {
        var withStrategy = strategy == LoadStrategy.UNKNOWN ? loadStrategy : strategy;
        LinkedHashSet<UriTemplate> withTemplates;
        if (uris.length == 1 && "".equals(uris[0])) {
            withTemplates = templates;
        } else {
            withTemplates = new LinkedHashSet<>();
            for (String uri : uris) {
                withTemplates.add(new UriTemplate(uri));
            }
        }
        return load(classLoader, withStrategy, withTemplates);
    }

    private ConfigHolder load(ClassLoader classLoader, LoadStrategy strategy, LinkedHashSet<UriTemplate> uriTemplates) {
        var values = uriTemplates.stream()
            .map(template -> {
              var uri = template.uri();
              var loader = loaders.stream().filter(l -> l.accept(uri)).findFirst()
                      .orElseThrow(() -> new UnsupportedOperationException(msg(LOADER_NOT_FOUND, uri)));
              return load(classLoader, loader, template.cachable(), uri);
            })
            .collect(Collectors.toList());
        return new ConfigHolder(strategy.combine(values, properties));
    }

    private Map<String, String> load(ClassLoader classLoader, Loader loader, boolean cachable, URI uri) {
        validateContentHashCode(classLoader, loader, uri);
        if (cachable) {
            return cache.computeIfAbsent(uri, u -> loader.load(u, classLoader));
        }
        return loader.load(uri, classLoader);
    }

    private void validateContentHashCode(ClassLoader classLoader, Loader loader, URI uri) {
        var newHashCode = loader.contentHashCode(uri, classLoader);
        var currentHashCode = hashCodes.get(uri);
        if (currentHashCode == null) {
            hashCodes.putIfAbsent(uri, newHashCode);
        } else if (currentHashCode != newHashCode) {
            cache.remove(uri);
            hashCodes.put(uri, newHashCode);
        }
    }
}
