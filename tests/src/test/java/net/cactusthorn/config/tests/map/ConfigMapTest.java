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
package net.cactusthorn.config.tests.map;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

class ConfigMapTest {

    @Test void map() {
        var properties = Map.of("map", "A|10,BBB|20", "sortedMap", "A|50,BBB|60");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigMap.class);
        var result = config.map();
        assertEquals(10, result.get("A"));
        assertEquals(20, result.get("BBB"));
        assertFalse(config.map2().isPresent());
    }

    @Test void optionalMap() {
        var properties = Map.of("map", "A|10,BBB|20", "sortedMap", "A|50,BBB|60", "map2", "10000|10;20000|20");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigMap.class);
        var result = config.map2().get();
        assertEquals((byte) 10, result.get(10000));
        assertEquals((byte) 20, result.get(20000));
    }

    @Test void defaultMap() throws MalformedURLException {
        var properties = Map.of("map", "A|10,BBB|20", "sortedMap", "A|50,BBB|60");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigMap.class);
        assertEquals(new URL("https://github.com"), config.map3().get(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
    }

    @Test void sortedMap() {
        var properties = Map.of("map", "A|10,BBB|20", "sortedMap", "A|50,BBB|60");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigMap.class);
        var result = config.sortedMap();
        assertEquals(50, result.get("A"));
        assertEquals(60, result.get("BBB"));
        assertFalse(config.map2().isPresent());
    }

    @Test void optionalSortedMap() {
        var properties = Map.of("map", "A|10,BBB|20", "sortedMap", "A|50,BBB|60", "sortedMap2", "10000|50;20000|60");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigMap.class);
        var result = config.sortedMap2().get();
        assertEquals((byte) 50, result.get(10000));
        assertEquals((byte) 60, result.get(20000));
    }

    @Test void defaultSortedMap() throws MalformedURLException {
        var properties = Map.of("map", "A|10,BBB|20", "sortedMap", "A|50,BBB|60");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigMap.class);
        assertEquals(new URL("https://github.com"), config.sortedMap3().get(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
    }

    @Test void defaultKeyConverter() {
        var properties = Map.of("map", "A|10,BBB|20", "sortedMap", "A|50,BBB|60", "defaultKeyConverter", "2007-12-03T10:15:30.00Z|Test");
        var config = ConfigFactory.builder().setSource(properties).build().create(ConfigMap.class);
        assertEquals("Test", config.defaultKeyConverter().get().get(Instant.parse("2007-12-03T10:15:30.00Z")));
    }
}