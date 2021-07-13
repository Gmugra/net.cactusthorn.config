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

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.cactusthorn.config.core.Reloadable;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.util.ConfigInitializer;

/**
 * The ConfigFactory is thread-safe, but not stateless.<br>
 * It stores loaded properties in the internal cache, and also control auto
 * reloading.<br>
 * Therefore, it certainly makes sense to create and use one single instance of
 * ConfigFactory for the whole application.<br>
 *
 * <h3>Example</h3>
 *
 * <pre>
 * &#064;Config public interface MyConfiguration {
 *
 *     String myValue();
 * }
 * </pre>
 *
 * <pre>
 * {
 *     &#64;code MyConfiguration myConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
 *          .addSource("file:./myconfig.properties")
 *          .addSource("classpath:config/myconfig.properties", "system:properties").build().create(MyConfiguration.class);
 * }
 * </pre>
 *
 * <h3>Caching</h3> By default, ConfigFactory caches loaded properties using
 * source-URI<br>
 * (after resolving system properties and/or environment variable in it) as a
 * cache key.<br>
 * To not cache properties related to the URI(s), use URI-prefix <b>nocache:</b>
 * this will switch off caching for the URI. e.g.:<br>
 * <ul>
 * <li><b>nocache:system:properties</b>
 * <li><b>nocache:file:~/my.properties</b>
 * </ul>
 *
 * <h3>Direct access to properties</h3> It's possible to get loaded properties
 * without define config-interface using
 * {@link net.cactusthorn.config.core.loader.ConfigHolder}:
 *
 * <pre>
 * {
 *     &#64;code ConfigHolder holder = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST).addSource("file:./myconfig.properties")
 *             .addSource("classpath:config/myconfig.properties", "system:properties").build().configHolder();
 *
 *     String val = holder.getString("app.val", "unknown");
 *     int intVal = holder.getInt("app.number");
 *     Optional<List<UUID>> ids = holder.getOptionalList(UUID::fromString, "ids", ",");
 *     Set<TimeUnit> units = holder.getSet(TimeUnit::valueOf, "app.units", "[:;]", "DAYS:HOURS");
 * }
 * </pre>
 *
 * <h3>Manually added properties</h3> The {@link ConfigFactory.Builder} contains
 * a method for adding properties manually:
 * {@link ConfigFactory.Builder#setSource}.<br>
 * Manually added properties are highest priority always: loaded by URIs
 * properties merged with manually added properties,<br>
 * independent of loading strategy. In other words: the manually added
 * properties will always override (sure, when the property keys are same)
 * properties loaded by URI(s).
 *
 * @author Alexei Khatskevich
 */
public final class ConfigFactory extends ConfigFactoryAncestor {

    private static final MethodType CONFIG_CONSTRUCTOR = MethodType.methodType(void.class, Loaders.class);
    private static final ConcurrentHashMap<Class<?>, MethodHandle> BUILDERS = new ConcurrentHashMap<>();

    private ConfigFactory(Loaders loaders) {
        super(loaders);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ConfigFactoryBuilder {

        private Builder() {
            super();
        }

        @Override public Builder addLoader(Loader loader) {
            return (Builder) super.addLoader(loader);
        }

        @Override public Builder addLoader(Class<? extends Loader> loaderClass) {
            return (Builder) super.addLoader(loaderClass);
        }

        @Override public Builder setLoadStrategy(LoadStrategy strategy) {
            return (Builder) super.setLoadStrategy(strategy);
        }

        @Override public Builder setSource(Map<String, String> properties) {
            return (Builder) super.setSource(properties);
        }

        @Override public Builder addSource(URI... uri) {
            return (Builder) super.addSource(uri);
        }

        @Override public Builder addSource(String... uri) {
            return (Builder) super.addSource(uri);
        }

        @Override public Builder autoReload(long periodInSeconds) {
            return (Builder) super.autoReload(periodInSeconds);
        }

        @Override public ConfigFactory build() {
            return new ConfigFactory(createLoaders());
        }
    }

    public <T> T create(Class<T> sourceInterface) {
        try {
            MethodHandle methodHandler = BUILDERS.computeIfAbsent(sourceInterface, this::findConfigConstructor);
            T configImpl = (T) methodHandler.invoke(loaders());
            if (configImpl instanceof Reloadable) {
                loaders().register((Reloadable) configImpl);
            }
            return configImpl;
        } catch (Throwable e) {
            throw new IllegalArgumentException(msg(CANT_INVOKE_CONFIGBUILDER, sourceInterface.getName()), e);
        }
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
