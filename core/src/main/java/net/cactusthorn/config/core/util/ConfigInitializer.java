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
package net.cactusthorn.config.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.loader.Loaders;

public abstract class ConfigInitializer {

    public static final String CONFIG_CLASSNAME_PREFIX = "Config_";
    public static final String INITIALIZER_CLASSNAME_PREFIX = "ConfigInitializer_";

    protected static final ConcurrentHashMap<Class<?>, Converter<?>> CONVERTERS = new ConcurrentHashMap<>();

    private final Loaders loaders;

    protected ConfigInitializer(Loaders loaders) {
        this.loaders = loaders;
    }

    public abstract Map<String, Object> initialize();

    @SuppressWarnings("unchecked") protected <T> T convert(Class<? extends Converter<T>> clazz, String value, String[] parameters) {
        return (T) CONVERTERS.get(clazz).convert(value, parameters);
    }

    protected Loaders loaders() {
        return loaders;
    }

    protected String globalPrefix(String key) {
        if (loaders.globalPrefix() != null) {
            return loaders.globalPrefix() + '.' + key;
        }
        return key;
    }

    @SuppressWarnings({"rawtypes", "unchecked"}) protected String expandKey(String key) {
        if (key.indexOf('{') == -1) {
            return key;
        }
        Map<String, String> values = new HashMap<>(System.getenv());
        values.putAll((Map) System.getProperties());
        String result = new VariablesParser(key).replace(values);
        result = result.replaceAll("\\.+", ".").replaceAll("^\\.+", "").replaceAll("\\.+$", "");
        return result;
    }
}
