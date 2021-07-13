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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.core.loader.Loaders;

public class OverrideFactoryTest {

    public static final class MyFactory extends ConfigFactoryAncestor {

        private MyFactory(Loaders loaders) {
            super(loaders);
        }

        public String someMethod() {
            return "AAAA";
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

            @Override public MyFactory build() {
                return new MyFactory(createLoaders());
            }
        }
    }

    @Test
    public void checkIt() {
        Map<String, String> properties = new HashMap<>();
        properties.put("ddd", "125");
        MyFactory factory = MyFactory.builder().setSource(properties).build();
        ConfigHolder holder = factory.configHolder();
        assertEquals("125", holder.getString("ddd"));
        assertEquals("AAAA", factory.someMethod());
    }
}
