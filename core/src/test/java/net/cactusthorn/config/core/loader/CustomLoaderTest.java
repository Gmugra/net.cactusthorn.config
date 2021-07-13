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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

public class CustomLoaderTest {

    public static final class TestLoader implements Loader {
        @Override public boolean accept(URI uri) {
            return uri.toString().equals("system:properties");
        }

        @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
            Map<String, String> result = new HashMap<>();
            result.put("testKey", "FromTestLoader");
            return result;
        }
    }

    static final class NotPublic implements Loader {
        @Override public boolean accept(URI uri) {
            return uri.toString().equals("system:properties");
        }

        @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
            return null;
        }
    }

    @Test public void standard() {
        System.setProperty("testKey", "realValue");
        ConfigHolder holder = ConfigFactory.builder().addSource("nocache:system:properties").build().configHolder();
        assertEquals("realValue", holder.getString("testKey"));
    }

    @Test public void custom() {
        System.setProperty("testKey", "realValue");
        ConfigHolder holder = ConfigFactory.builder().addLoader(new TestLoader()).addSource("nocache:system:properties").build()
                .configHolder();
        assertEquals("FromTestLoader", holder.getString("testKey"));
    }

    @Test public void customAsClass() {
        System.setProperty("testKey", "realValue");
        ConfigHolder holder = ConfigFactory.builder().addLoader(TestLoader.class).addSource("nocache:system:properties").build()
                .configHolder();
        assertEquals("FromTestLoader", holder.getString("testKey"));
    }

    @Test public void cantInvoke() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addLoader(NotPublic.class));
    }

    @Test public void addLoaderNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addLoader((Loader) null));
    }

    @Test public void addLoaderClassNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addLoader((Class<? extends Loader>) null));
    }
}
