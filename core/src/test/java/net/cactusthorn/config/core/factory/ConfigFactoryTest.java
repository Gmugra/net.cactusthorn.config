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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.TestConfig;
import net.cactusthorn.config.core.loader.LoadStrategy;

class ConfigFactoryTest {

    @Test void map() {
        var properties = Map.of("test.string", "TEST", "test.list", "A,B,C", "test.set", "A,B,C,C", "test.sort",
            "A,B,C,C", "test.url", "https://google.com");
        var testConfig = ConfigFactory.builder().setSource(properties).build().create(TestConfig.class);
        assertEquals("TEST", testConfig.str());
    }

    @Test void listNoDefault() {
        var properties =  Map.of("test.string", "TEST", "test.set", "A,B,C,C", "test.sort", "A,B,C,C");
        var factory = ConfigFactory.builder().setSource(properties).build();
        var testConfig = factory.create(TestConfig.class);
        assertTrue(testConfig.list().isEmpty());
    }

    @Test void setNoDefault() {
        var properties =  Map.of("test.string", "TEST", "test.list", "A,B,C", "test.sort", "A,B,C,C");
        var factory = ConfigFactory.builder().setSource(properties).build();
        var testConfig = factory.create(TestConfig.class);
        assertTrue(testConfig.set().isEmpty());
    }

    @Test void sortNoDefault() {
        var properties =  Map.of("test.string", "TEST", "test.list", "A,B,C", "test.set", "A,B,C,C");
        var factory = ConfigFactory.builder().setSource(properties).build();
        var testConfig = factory.create(TestConfig.class);
        assertTrue(testConfig.sort().isEmpty());
    }

    @Test void classpath() {
        var uri = URI.create("classpath:config/testconfig.properties");
        var testConfig = ConfigFactory.builder().addSource(uri).build().create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test void classpathFromString() {
        TestConfig testConfig = ConfigFactory.builder().addSource("classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test void classpathAndMap() {
        var uri = URI.create("classpath:config/testconfig.properties");
        var properties = Map.of("test.string", "TEST");
        var testConfig = ConfigFactory.builder().addSource(uri).setSource(properties).build().create(TestConfig.class);
        assertEquals("TEST", testConfig.str());
    }

    @Test void systemProperty() {
        System.setProperty("testit", "config");
        var testConfig = ConfigFactory.builder().addSource("classpath:{testit}/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test void systemPropertyWithDefault() {
        System.clearProperty("testit");
        var testConfig = ConfigFactory.builder().addSource("classpath:{testit:config}/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test void userHome() throws IOException {
        var userHome = java.nio.file.Paths.get(System.getProperty("user.home"));
        var file = userHome.resolve("standard-properties.xml");
        try (var stream = ConfigFactoryTest.class.getClassLoader().getResourceAsStream("standard-properties.xml")) {
            Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
        }
        var holder = ConfigFactory.builder().addSource("file:~/standard-properties.xml").build().configHolder();
        assertEquals("foobar", holder.getString("server.http.hostname"));
    }

    @Test void userHomeURI() throws IOException {
        var userHome = java.nio.file.Paths.get(System.getProperty("user.home"));
        var file = userHome.resolve("standard-properties.xml");
        try (var stream = ConfigFactoryTest.class.getClassLoader().getResourceAsStream("standard-properties.xml")) {
            Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
        }
        var holder = ConfigFactory.builder().addSource(URI.create("file:~/standard-properties.xml")).build().configHolder();
        assertEquals("foobar", holder.getString("server.http.hostname"));
    }

    @Test void merge() {
        var testConfig = ConfigFactory.builder().addSource("classpath:config/testconfig.properties", "classpath:test.properties")
                .build().create(TestConfig.class);
        assertEquals("bbb", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test void merge2() {
        System.setProperty("aaa", "PROP");
        var testConfig = ConfigFactory.builder()
                .addSource("classpath:config/testconfig.properties", "system:properties", "classpath:test.properties").build()
                .create(TestConfig.class);
        assertEquals("PROP", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test void first() {
        System.setProperty("aaa", "PROP");
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "system:properties", "classpath:test.properties").build()
                .create(TestConfig.class);
        assertEquals("ddd", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test void customConverter() {
        var testConfig = ConfigFactory.builder().addSource("classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("test::default", testConfig.testconverter());
    }

    @Test void setSourceNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setSource(null));
    }

    @Test void addSourceUriNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource((URI[]) null));
    }

    @Test void addSourceStrNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource((String[]) null));
    }

    @Test void addSourceUriEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource(new URI[0]));
    }

    @Test void addSourceStrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource(new String[0]));
    }

    @Test void addSourceStrNullElement() {
        ConfigFactory.builder().addSource(new String[] {null});
    }

    @Test void loaderNotFound() {
        var factory = ConfigFactory.builder().addSource("system:yyy").build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(TestConfig.class));
    }

    @Test void unknownBuilder() {
        var factory = ConfigFactory.builder().build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(String.class));
    }

    @Test void globalPrefixNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setGlobalPrefix(null));
    }

    @Test void globalPrefixEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setGlobalPrefix(""));
    }

    @Test void globalPrefix() {
        var properties = Map.of("gp.test.string", "TEST", "gp.test.list", "A,B,C", "gp.test.set", "A,B,C,C",
            "gp.test.sort", "A,B,C,C", "gp.test.url", "https://google.com");

        ConfigFactory.builder().setGlobalPrefix("gp").build();
        var testConfig = ConfigFactory.builder().setSource(properties).setGlobalPrefix("gp").build().create(TestConfig.class);
        assertEquals("TEST", testConfig.str());
    }
}
