package net.cactusthorn.config.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    }

    @Test public void getInt() {
        assertEquals(10, holder.getInt("int"));
    }

    @Test public void getShort() {
        assertEquals((short) 20, holder.getShort("short"));
    }

    @Test public void getByte() {
        assertEquals((byte) 15, holder.getByte("byte"));
    }

    @Test public void getLong() {
        assertEquals(30L, holder.getLong("long"));
    }

    @Test public void getBoolean() {
        assertTrue(holder.getBoolean("bool"));
    }

    @Test public void getFloat() {
        assertEquals(125.5f, holder.getFloat("float"));
    }

    @Test public void getDouble() {
        assertEquals(137.45d, holder.getDouble("double"));
    }

    @Test public void contains() {
        assertTrue(holder.contains("double"));
    }

    @Test public void getChar() {
        assertEquals('X', holder.getChar("char"));
    }

    @Test public void getProperties() {
        assertFalse(holder.getProperties().isEmpty());
    }

    @Test public void list() {
        Optional<List<UUID>> list = holder.getOptionalList(UUID::fromString, "list", ",");
        assertEquals(2, list.get().size());
    }
}
