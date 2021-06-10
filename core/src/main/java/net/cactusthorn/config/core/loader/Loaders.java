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

        @SuppressWarnings({"rawtypes", "unchecked"}) URI uri() {
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

        private URI replace(URI u) {
            String tmp = replace(u.toString());
            return URI.create(tmp);
        }

        private static final String NOCACHE = "nocache:";
        private static final String USER_HOME = "user.home";

        private String replace(String str) {
            String result = str;
            if (result.indexOf(NOCACHE) == 0) {
                this.cachable = false;
                result = result.substring(NOCACHE.length());
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

    public ConfigHolder load(ClassLoader classLoader) {
        return load(classLoader, loadStrategy, templates);
    }

    public ConfigHolder load(ClassLoader classLoader, LoadStrategy strategy, String[] uris) {
        LoadStrategy withStrategy = strategy == LoadStrategy.UNKNOWN ? loadStrategy : strategy;
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
        List<Map<String, String>> values = new ArrayList<>();
        for (UriTemplate template : uriTemplates) {
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
        return new ConfigHolder(strategy.combine(values, properties));
    }
}
