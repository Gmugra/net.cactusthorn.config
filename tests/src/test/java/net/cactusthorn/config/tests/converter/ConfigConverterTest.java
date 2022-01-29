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
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

public class ConfigConverterTest {

    @Test public void url() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(URI.create("cactusthorn.net"), config.url());
    }

    @Test public void optionalUrl() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        properties.put("ourl", "https://www.bing.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(URI.create("cactusthorn.net"), config.ourl().get());
    }

    @Test public void list() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        properties.put("listURI", "https://www.bing.com,https://www.google.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(2, config.listURI().get().size());
    }

    @Test public void listPath() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        properties.put("listPath", "my/home,some/dir");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(2, config.listPath().get().size());
    }

    @Test public void defaultUrl() throws MalformedURLException {
        ConfigConverter config = ConfigFactory.builder().build().create(ConfigConverter.class);
        assertEquals(URI.create("cactusthorn.net"), config.url());
    }

    @Test public void defaultConverter() throws MalformedURLException {
        ConfigConverter config = ConfigFactory.builder().build().create(ConfigConverter.class);
        assertEquals(URI.create("https://github.com"), config.defaultConverter());
    }

    @Test public void myInterface() {
        Map<String, String> properties = new HashMap<>();
        properties.put("myInterface", "?");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals("MY_VALUE", config.myInterface().get().getValue());
    }

    @Test public void myInterfaceList() {
        Map<String, String> properties = new HashMap<>();
        properties.put("myInterfaceList", "?");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals("MY_VALUE", config.myInterfaceList().get().get(0).getValue());
    }

    @Test public void myAbstractClass() {
        Map<String, String> properties = new HashMap<>();
        properties.put("myAbstractClass", "?");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals("MY_A_VALUE", config.myAbstractClass().get().getValue());
    }

    @Test public void myAbstractClassList() {
        Map<String, String> properties = new HashMap<>();
        properties.put("myAbstractClassList", "?");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals("MY_A_VALUE", config.myAbstractClassList().get().get(0).getValue());
    }

    @Test public void defaulConvertersMap() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("defaulConvertersMap", "https://www.bing.com|https://www.google.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);

        assertEquals(URI.create("https://www.google.com"), config.defaulConvertersMap().get().get(new URL("https://www.bing.com")));
    }

    @Test public void defaulConvertersPathMap() {
        Map<String, String> properties = new HashMap<>();
        properties.put("defaulConvertersPathMap", "my/home|https://www.google.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);

        assertEquals(URI.create("https://www.google.com"), config.defaulConvertersPathMap().get().get(Paths.get("my/home")));
    }

    @Test public void defaulConvertersPathMap2() {
        Map<String, String> properties = new HashMap<>();
        properties.put("defaulConvertersPathMap2", "https://www.google.com|my/home");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);

        assertEquals(Paths.get("my/home"), config.defaulConvertersPathMap2().get().get(URI.create("https://www.google.com")));
    }

    @Test public void myInterfaceMap() {
        Map<String, String> properties = new HashMap<>();
        properties.put("myInterfaceMap", "A|?");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);

        assertEquals("MY_VALUE", config.myInterfaceMap().get().get("A").getValue());
    }

    @Test public void myInterfaceMap2() {
        Map<String, String> properties = new HashMap<>();
        properties.put("myInterfaceMap2", "my/home|?");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);

        assertEquals("MY_VALUE", config.myInterfaceMap2().get().get(Paths.get("my/home")).getValue());
    }

    @Test public void myAbstractClassMap() {
        Map<String, String> properties = new HashMap<>();
        properties.put("myAbstractClassMap", "A|?");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);

        assertEquals("MY_A_VALUE", config.myAbstractClassMap().get().get("A").getValue());
    }

    @Test public void myAbstractClassMap2() {
        Map<String, String> properties = new HashMap<>();
        properties.put("myAbstractClassMap2", "my/home|?");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);

        assertEquals("MY_A_VALUE", config.myAbstractClassMap2().get().get(Paths.get("my/home")).getValue());
    }
}
