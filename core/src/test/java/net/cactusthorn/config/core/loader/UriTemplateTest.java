package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

public class UriTemplateTest {

    @Test public void nocacheStr() {
        Loaders.UriTemplate template = new Loaders.UriTemplate("nocache:system:properties", true);
        assertFalse(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }

    @Test public void nocacheURI() {
        Loaders.UriTemplate template = new Loaders.UriTemplate(URI.create("nocache:system:properties"), true);
        assertFalse(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }

    @Test public void cacheStr() {
        Loaders.UriTemplate template = new Loaders.UriTemplate("system:properties", true);
        assertTrue(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }

    @Test public void cacheURI() {
        Loaders.UriTemplate template = new Loaders.UriTemplate(URI.create("system:properties"), true);
        assertTrue(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }

    @Test public void nocacheParamStr() {
        Loaders.UriTemplate template = new Loaders.UriTemplate("system:properties", false);
        assertFalse(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }

    @Test public void nocacheParamURI() {
        Loaders.UriTemplate template = new Loaders.UriTemplate(URI.create("system:properties"), false);
        assertFalse(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }
}
