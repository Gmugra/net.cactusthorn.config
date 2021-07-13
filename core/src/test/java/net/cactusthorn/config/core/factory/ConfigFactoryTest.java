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
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.TestConfig;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;

public class ConfigFactoryTest {

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void map() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.list", "A,B,C");
        properties.put("test.set", "A,B,C,C");
        properties.put("test.sort", "A,B,C,C");
        properties.put("test.url", "https://google.com");
        TestConfig testConfig = ConfigFactory.builder().setSource(properties).build().create(TestConfig.class);
        assertEquals("TEST", testConfig.str());
    }

    @Test public void listNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.set", "A,B,C,C");
        properties.put("test.sort", "A,B,C,C");
        ConfigFactory factory = ConfigFactory.builder().setSource(properties).build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(TestConfig.class));
    }

    @Test public void setNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.list", "A,B,C");
        properties.put("test.sort", "A,B,C,C");
        ConfigFactory factory = ConfigFactory.builder().setSource(properties).build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(TestConfig.class));
    }

    @Test public void sortNoDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        properties.put("test.list", "A,B,C");
        properties.put("test.set", "A,B,C,C");
        ConfigFactory factory = ConfigFactory.builder().setSource(properties).build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(TestConfig.class));
    }

    @Test public void classpath() {
        URI uri = URI.create("classpath:config/testconfig.properties");
        TestConfig testConfig = ConfigFactory.builder().addSource(uri).build().create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void classpathFromString() {
        TestConfig testConfig = ConfigFactory.builder().addSource("classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void classpathAndMap() {
        URI uri = URI.create("classpath:config/testconfig.properties");
        Map<String, String> properties = new HashMap<>();
        properties.put("test.string", "TEST");
        TestConfig testConfig = ConfigFactory.builder().addSource(uri).setSource(properties).build().create(TestConfig.class);
        assertEquals("TEST", testConfig.str());
    }

    @Test public void systemProperty() {
        System.setProperty("testit", "config");
        TestConfig testConfig = ConfigFactory.builder().addSource("classpath:{testit}/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void userHome() throws IOException {
        Path userHome = java.nio.file.Paths.get(System.getProperty("user.home"));
        Path file = userHome.resolve("standard-properties.xml");
        try (InputStream stream = ConfigFactoryTest.class.getClassLoader().getResourceAsStream("standard-properties.xml")) {
            Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
        }
        ConfigHolder holder = ConfigFactory.builder().addSource("file:~/standard-properties.xml").build().configHolder();
        assertEquals("foobar", holder.getString("server.http.hostname"));
    }

    @Test public void userHomeURI() throws IOException {
        Path userHome = java.nio.file.Paths.get(System.getProperty("user.home"));
        Path file = userHome.resolve("standard-properties.xml");
        try (InputStream stream = ConfigFactoryTest.class.getClassLoader().getResourceAsStream("standard-properties.xml")) {
            Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
        }
        ConfigHolder holder = ConfigFactory.builder().addSource(URI.create("file:~/standard-properties.xml")).build().configHolder();
        assertEquals("foobar", holder.getString("server.http.hostname"));
    }

    @Test public void merge() {
        TestConfig testConfig = ConfigFactory.builder().addSource("classpath:config/testconfig.properties", "classpath:test.properties")
                .build().create(TestConfig.class);
        assertEquals("bbb", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void merge2() {
        System.setProperty("aaa", "PROP");
        TestConfig testConfig = ConfigFactory.builder()
                .addSource("classpath:config/testconfig.properties", "system:properties", "classpath:test.properties").build()
                .create(TestConfig.class);
        assertEquals("PROP", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void first() {
        System.setProperty("aaa", "PROP");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "system:properties", "classpath:test.properties").build()
                .create(TestConfig.class);
        assertEquals("ddd", testConfig.aaa());
        assertEquals("RESOURCE", testConfig.str());
    }

    @Test public void customConverter() {
        TestConfig testConfig = ConfigFactory.builder().addSource("classpath:config/testconfig.properties").build()
            .create(TestConfig.class);
        assertEquals("test::default", testConfig.testconverter());
    }

    @Test public void setSourceNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setSource(null));
    }

    @Test public void addSourceUriNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource((URI[]) null));
    }

    @Test public void addSourceStrNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource((String[]) null));
    }

    @Test public void addSourceUriEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource(new URI[0]));
    }

    @Test public void addSourceStrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().addSource(new String[0]));
    }

    @Test public void addSourceStrNullElement() {
        ConfigFactory.builder().addSource(new String[] { null });
    }

    @Test public void loaderNotFound() {
        ConfigFactory factory = ConfigFactory.builder().addSource("system:yyy").build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(TestConfig.class));
    }

    @Test public void unknownBuilder() {
        ConfigFactory factory = ConfigFactory.builder().build();
        assertThrows(IllegalArgumentException.class, () -> factory.create(String.class));
    }
}
