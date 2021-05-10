package net.cactusthorn.config.core.loader.standard;

import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import net.cactusthorn.config.core.loader.Loader;

public class ClasspathJarManifestLoader implements Loader {

    private static final Logger LOG = Logger.getLogger(ClasspathJarManifestLoader.class.getName());

    private static final String SUB_PREFIX = "jar:manifest?";
    private static final String PREFIX = "classpath:" + SUB_PREFIX;

    @Override public boolean accept(URI uri) {
        return uri.toString().startsWith(PREFIX);
    }

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {

        String param = uri.getSchemeSpecificPart().substring(SUB_PREFIX.length());
        String[] parts = param.split("=", 2);
        String name = parts[0];
        String value = parts.length > 1 ? parts[1] : null;

        try {
            Enumeration<URL> urls = getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                try (InputStream in = url.openStream()) {

                    Manifest manifest = new Manifest(in);
                    Attributes attributes = manifest.getMainAttributes();
                    String attribute = attributes.getValue(name);
                    if (attribute != null && (value == null || value.equals(attribute))) {
                        return attributes.entrySet().stream()
                                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));
                    }
                }
            }
        } catch (IOException e) {
            LOG.info(msg(CANT_LOAD_RESOURCE, uri.toString(), e.toString()));
            return Collections.emptyMap();
        }

        if (value != null) {
            LOG.info(msg(MANIFEST_NOT_FOUND_1, name, value));
            return Collections.emptyMap();
        }
        LOG.info(msg(MANIFEST_NOT_FOUND_2, name));
        return Collections.emptyMap();
    }

}
