package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class ConfigHolderTest {

    static ConfigHolder holder;

    @BeforeAll static void setUp() {
        Map<String, String> properties = new HashMap<>();
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
        holder = ConfigFactory.builder().setSource(properties).build().configHolder();
    }

    @Test public void getString() {
        assertEquals("ABC", holder.getString("string"));
        assertEquals("default", holder.getString("notExtsts", "default"));
        assertEquals("ABC", holder.getString("string", "default"));
        assertFalse(holder.getOptionalString("notExtsts").isPresent());
        assertEquals("ABC", holder.getOptionalString("string").get());
    }

    @Test public void getInt() {
        assertEquals(10, holder.getInt("int"));
        assertEquals(20, holder.getInt("notExtsts", 20));
        assertEquals(10, holder.getInt("int", 20));
        assertFalse(holder.getOptionalInt("notExtsts").isPresent());
        assertEquals(10, holder.getOptionalInt("int").get());
    }

    @Test public void getShort() {
        assertEquals((short) 20, holder.getShort("short"));
        assertEquals((short) 10, holder.getShort("notExtsts", (short) 10));
        assertEquals((short) 20, holder.getShort("short", (short) 10));
        assertFalse(holder.getOptionalShort("notExtsts").isPresent());
        assertEquals((short) 20, holder.getOptionalShort("short").get());
    }

    @Test public void getByte() {
        assertEquals((byte) 15, holder.getByte("byte"));
        assertEquals((byte) 20, holder.getByte("notExtsts", (byte) 20));
        assertEquals((byte) 15, holder.getByte("byte", (byte) 20));
        assertFalse(holder.getOptionalByte("notExtsts").isPresent());
        assertEquals((byte) 15, holder.getOptionalByte("byte").get());
    }

    @Test public void getLong() {
        assertEquals(30L, holder.getLong("long"));
        assertEquals(20L, holder.getLong("notExtsts", 20L));
        assertEquals(30L, holder.getLong("long", 20L));
        assertFalse(holder.getOptionalLong("notExtsts").isPresent());
        assertEquals(30L, holder.getOptionalLong("long").get());
    }

    @Test public void getBoolean() {
        assertTrue(holder.getBoolean("bool"));
        assertTrue(holder.getBoolean("notExtsts", true));
        assertTrue(holder.getBoolean("bool", false));
        assertFalse(holder.getOptionalBoolean("notExtsts").isPresent());
        assertTrue(holder.getOptionalBoolean("bool").get());
    }

    @Test public void getFloat() {
        assertEquals(125.5f, holder.getFloat("float"));
        assertEquals(133.4f, holder.getFloat("notExtsts", 133.4f));
        assertEquals(125.5f, holder.getFloat("float", 133.4f));
        assertFalse(holder.getOptionalFloat("notExtsts").isPresent());
        assertEquals(125.5f, holder.getOptionalFloat("float").get());
    }

    @Test public void getDouble() {
        assertEquals(137.45d, holder.getDouble("double"));
        assertEquals(133.4d, holder.getDouble("notExtsts", 133.4d));
        assertEquals(137.45d, holder.getDouble("double", 133.4d));
        assertFalse(holder.getOptionalDouble("notExtsts").isPresent());
        assertEquals(137.45d, holder.getOptionalDouble("double").get());
    }

    @Test public void contains() {
        assertTrue(holder.contains("double"));
    }

    @Test public void getChar() {
        assertEquals('X', holder.getChar("char"));
        assertEquals('D', holder.getChar("notExtsts", 'D'));
        assertEquals('X', holder.getChar("char", 'D'));
        assertFalse(holder.getOptionalChar("notExtsts").isPresent());
        assertEquals('X', holder.getOptionalChar("char").get());
    }

    @Test public void getProperties() {
        assertFalse(holder.getProperties().isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> holder.getProperties().clear());
    }

    @Test public void list() {
        Optional<List<UUID>> list = holder.getOptionalList(UUID::fromString, "list", ",");
        assertEquals(2, list.get().size());
    }
}
