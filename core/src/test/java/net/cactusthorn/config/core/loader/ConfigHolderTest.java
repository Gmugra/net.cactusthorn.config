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

import static org.junit.jupiter.api.Assertions.*;
import static java.util.Optional.of;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

class ConfigHolderTest {

    static ConfigHolder holder;

    @BeforeAll static void setUp() {
        var properties = new HashMap<String, String>();
        properties.put("string", "ABC");
        properties.put("int", "10");
        properties.put("byte", "15");
        properties.put("short", "20");
        properties.put("long", "30");
        properties.put("bool", "true");
        properties.put("float", "125.5");
        properties.put("double", "137.45");
        properties.put("char", "XYZ");
        properties.put("list", "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454,123e4567-e89b-12d3-a456-556642440000");
        properties.put("map", "A|10,B|20");
        properties.put("sortedMap", "A|50,B|60");
        holder = ConfigFactory.builder().setSource(properties).build().configHolder();
    }

    @Test void getString() {
        assertEquals("ABC", holder.getString("string"));
        assertEquals("default", holder.getString("notExtsts", "default"));
        assertEquals("ABC", holder.getString("string", "default"));
        assertFalse(holder.getOptionalString("notExtsts").isPresent());
        assertEquals(of("ABC"), holder.getOptionalString("string"));
    }

    @Test void getInt() {
        assertEquals(10, holder.getInt("int"));
        assertEquals(20, holder.getInt("notExtsts", 20));
        assertEquals(10, holder.getInt("int", 20));
        assertFalse(holder.getOptionalInt("notExtsts").isPresent());
        assertEquals(of(10), holder.getOptionalInt("int"));
    }

    @Test void getShort() {
        assertEquals((short) 20, holder.getShort("short"));
        assertEquals((short) 10, holder.getShort("notExtsts", (short) 10));
        assertEquals((short) 20, holder.getShort("short", (short) 10));
        assertFalse(holder.getOptionalShort("notExtsts").isPresent());
        assertEquals(of((short) 20), holder.getOptionalShort("short"));
    }

    @Test void getByte() {
        assertEquals((byte) 15, holder.getByte("byte"));
        assertEquals((byte) 20, holder.getByte("notExtsts", (byte) 20));
        assertEquals((byte) 15, holder.getByte("byte", (byte) 20));
        assertFalse(holder.getOptionalByte("notExtsts").isPresent());
        assertEquals(of((byte) 15), holder.getOptionalByte("byte"));
    }

    @Test void getLong() {
        assertEquals(30L, holder.getLong("long"));
        assertEquals(20L, holder.getLong("notExtsts", 20L));
        assertEquals(30L, holder.getLong("long", 20L));
        assertFalse(holder.getOptionalLong("notExtsts").isPresent());
        assertEquals(of(30L), holder.getOptionalLong("long"));
    }

    @Test void getBoolean() {
        assertTrue(holder.getBoolean("bool"));
        assertTrue(holder.getBoolean("notExtsts", true));
        assertTrue(holder.getBoolean("bool", false));
        assertFalse(holder.getOptionalBoolean("notExtsts").isPresent());
        assertEquals(of(true), holder.getOptionalBoolean("bool"));
    }

    @Test void getFloat() {
        assertEquals(125.5f, holder.getFloat("float"));
        assertEquals(133.4f, holder.getFloat("notExtsts", 133.4f));
        assertEquals(125.5f, holder.getFloat("float", 133.4f));
        assertFalse(holder.getOptionalFloat("notExtsts").isPresent());
        assertEquals(of(125.5f), holder.getOptionalFloat("float"));
    }

    @Test void getDouble() {
        assertEquals(137.45d, holder.getDouble("double"));
        assertEquals(133.4d, holder.getDouble("notExtsts", 133.4d));
        assertEquals(137.45d, holder.getDouble("double", 133.4d));
        assertFalse(holder.getOptionalDouble("notExtsts").isPresent());
        assertEquals(of(137.45d), holder.getOptionalDouble("double"));
    }

    @Test void contains() {
        assertTrue(holder.contains("double"));
    }

    @Test void getChar() {
        assertEquals('X', holder.getChar("char"));
        assertEquals('D', holder.getChar("notExtsts", 'D'));
        assertEquals('X', holder.getChar("char", 'D'));
        assertFalse(holder.getOptionalChar("notExtsts").isPresent());
        assertEquals(of('X'), holder.getOptionalChar("char"));
    }

    @Test void getProperties() {
        assertFalse(holder.getProperties().isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> holder.getProperties().clear());
    }

    @Test void list() {
        var list = holder.getList(UUID::fromString, "list", ",");
        assertEquals(2, list.size());
    }

    @Test void getNotExist() {
        assertThrows(IllegalArgumentException.class, () -> holder.get(UUID::fromString, "notExist"));
    }

    @Test void duplicatedKey() {
        ConfigHolder h = ConfigFactory.builder().setSource(Map.of("map", "A|10,A|20")).build().configHolder();
        assertThrows(IllegalStateException.class, () -> h.getMap(s -> s, Integer::valueOf, "map", ","));
    }

    @Test void getMap() {
        var result = holder.getMap(s -> s, Integer::valueOf, "map", ",");
        assertEquals(10, result.get("A"));
        assertTrue(holder.getMap(s -> s, Integer::valueOf, "notExists", ",").isEmpty());
    }

    @Test void getMapDefault() {
        var result = holder.getMap(s -> s, Integer::valueOf, "map", ",", "C|30");
        assertEquals(10, result.get("A"));
        result = holder.getMap(s -> s, Integer::valueOf, "notExists", ",", "C|30");
        assertEquals(30, result.get("C"));
    }

    @Test void getOptionalMap() {
        var result = holder.getMap(s -> s, Integer::valueOf, "map", ",");
        assertEquals(10, result.get("A"));
        assertTrue(holder.getMap(s -> s, Integer::valueOf, "notExists", ",").isEmpty());
    }

    @Test void getSortedMap() {
        var result = holder.getSortedMap(s -> s, Integer::valueOf, "sortedMap", ",");
        assertEquals(50, result.get("A"));
        assertTrue(holder.getSortedMap(s -> s, Integer::valueOf, "notExists", ",").isEmpty());
    }

    @Test void getSortedMapDefault() {
        var result = holder.getSortedMap(s -> s, Integer::valueOf, "sortedMap", ",", "C|30");
        assertEquals(50, result.get("A"));
        result = holder.getSortedMap(s -> s, Integer::valueOf, "notExists", ",", "C|30");
        assertEquals(30, result.get("C"));
    }

    @Test void getOptionalSortedMap() {
        var result = holder.getSortedMap(s -> s, Integer::valueOf, "sortedMap", ",");
        assertEquals(50, result.get("A"));
        assertTrue(holder.getSortedMap(s -> s, Integer::valueOf, "notExists", ",").isEmpty());
    }
}
