package net.cactusthorn.config.extras.zookeeper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.Loader;

public class ZooKeeperLoaderAcceptTest {

    private static final Loader LOADER = new ZooKeeperLoader();

    @Test public void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("zookeeper://localhost:7777,localhost:8888/basePath")));
    }

    @Test public void acceptFull() {
        String uri = "zookeeper://localhost:7777,localhost:8888/basePath?sessionTimeoutMs=10000&connectionTimeoutMs=2000";
        assertTrue(LOADER.accept(URI.create(uri)));
    }

    @Test public void notAcceptOpaque() {
        assertFalse(LOADER.accept(URI.create("zookeeper:localhost")));
    }

    @Test public void notAcceptNoAuthority() {
        assertFalse(LOADER.accept(URI.create("zookeeper:///basePath")));
    }

    @Test public void notAcceptScheme() {
        assertFalse(LOADER.accept(URI.create("http://localhost:7777,localhost:8888/basePath")));
    }

    @Test public void notAcceptNoPath() {
        assertFalse(LOADER.accept(URI.create("zookeeper://localhost:7777,localhost:8888")));
    }
}
