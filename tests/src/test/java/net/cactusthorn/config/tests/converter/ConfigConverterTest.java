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
package net.cactusthorn.config.tests.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

class ConfigConverterTest {

    @Test void url() throws MalformedURLException {
        var config = ConfigFactory.builder().setSource(Map.of("url", "https://www.google.com")).build()
            .create(ConfigConverter.class);
        assertEquals(URI.create("cactusthorn.net"), config.url());
    }

    @Test void optionalUrl() throws MalformedURLException {
        var properties = Map.of("url", "https://www.google.com", "ourl", "https://www.bing.com"); 
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(Optional.of(URI.create("cactusthorn.net")), config.ourl());
    }

    @Test void list() throws MalformedURLException {
        var properties = Map.of("url", "https://www.google.com", "listURI", "https://www.bing.com,https://www.google.com"); 
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(2, config.listURI().size());
    }

    @Test void listPath() throws MalformedURLException {
        var properties = Map.of("url", "https://www.google.com", "listPath", "my/home,some/dir"); 
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(2, config.listPath().size());
    }

    @Test void defaultUrl() throws MalformedURLException {
        var config = ConfigFactory.builder().build().create(ConfigConverter.class);
        assertEquals(URI.create("cactusthorn.net"), config.url());
    }

    @Test void defaultConverter() throws MalformedURLException {
        var config = ConfigFactory.builder().build().create(ConfigConverter.class);
        assertEquals(URI.create("https://github.com"), config.defaultConverter());
    }

    @Test void myInterface() {
        var config = ConfigFactory.builder().setSource(Map.of("myInterface", "?")).build().create(ConfigConverter.class);
        assertEquals("MY_VALUE", config.myInterface().get().getValue());
    }

    @Test void myInterfaceList() {
        var config = ConfigFactory.builder().setSource(Map.of("myInterfaceList", "?")).build().create(ConfigConverter.class);
        assertEquals("MY_VALUE", config.myInterfaceList().get(0).getValue());
    }

    @Test void myAbstractClass() {
        var config = ConfigFactory.builder().setSource(Map.of("myAbstractClass", "?")).build().create(ConfigConverter.class);
        assertEquals("MY_A_VALUE", config.myAbstractClass().get().getValue());
    }

    @Test void myAbstractClassList() {
        var config = ConfigFactory.builder().setSource(Map.of("myAbstractClassList", "?")).build().create(ConfigConverter.class);
        assertEquals("MY_A_VALUE", config.myAbstractClassList().get(0).getValue());
    }

    @Test void defaulConvertersMap() throws MalformedURLException {
        var properties = Map.of("defaulConvertersMap", "https://www.bing.com|https://www.google.com");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(URI.create("https://www.google.com"), config.defaulConvertersMap().get(new URL("https://www.bing.com")));
    }

    @Test void defaulConvertersPathMap() {
        var properties = Map.of("defaulConvertersPathMap", "my/home|https://www.google.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(URI.create("https://www.google.com"), config.defaulConvertersPathMap().get(Paths.get("my/home")));
    }

    @Test void defaulConvertersPathMap2() {
        var properties = Map.of("defaulConvertersPathMap2", "https://www.google.com|my/home");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(Paths.get("my/home"), config.defaulConvertersPathMap2().get(URI.create("https://www.google.com")));
    }

    @Test void myInterfaceMap() {
        var config = ConfigFactory.builder().setSource(Map.of("myInterfaceMap", "A|?")).build().create(ConfigConverter.class);
        assertEquals("MY_VALUE", config.myInterfaceMap().get("A").getValue());
    }

    @Test void myInterfaceMap2() {
        var config = ConfigFactory.builder().setSource(Map.of("myInterfaceMap2", "my/home|?")).build().create(ConfigConverter.class);
        assertEquals("MY_VALUE", config.myInterfaceMap2().get(Paths.get("my/home")).getValue());
    }

    @Test void myAbstractClassMap() {
        var config = ConfigFactory.builder().setSource(Map.of("myAbstractClassMap", "A|?")).build().create(ConfigConverter.class);
        assertEquals("MY_A_VALUE", config.myAbstractClassMap().get("A").getValue());
    }

    @Test void myAbstractClassMap2() {
        var config = ConfigFactory.builder().setSource(Map.of("myAbstractClassMap2", "my/home|?")).build()
            .create(ConfigConverter.class);
        assertEquals("MY_A_VALUE", config.myAbstractClassMap2().get(Paths.get("my/home")).getValue());
    }
}
