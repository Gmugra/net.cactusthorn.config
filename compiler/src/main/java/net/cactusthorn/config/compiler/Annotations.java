package net.cactusthorn.config.compiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.lang.model.element.Element;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Default;
import net.cactusthorn.config.core.Disable;
import net.cactusthorn.config.core.Key;
import net.cactusthorn.config.core.Prefix;
import net.cactusthorn.config.core.Split;
import net.cactusthorn.config.core.loader.LoadStrategy;

public class Annotations {

    public final class ConfigInfo {
        private final String[] sources;
        private final LoadStrategy loadStrategy;

        private ConfigInfo(String[] sources, LoadStrategy loadStrategy) {
            this.sources = sources;
            this.loadStrategy = loadStrategy;
        }

        public String[] sources() {
            return sources;
        }

        public LoadStrategy loadStrategy() {
            return loadStrategy;
        }
    }

    private final Element element;

    public Annotations(Element element) {
        this.element = element;
    }

    public Optional<String> prefix() {
        return Optional.ofNullable(element.getAnnotation(Prefix.class)).map(a -> a.value());
    }

    public Optional<String> split() {
        return Optional.ofNullable(element.getAnnotation(Split.class)).map(a -> a.value());
    }

    public Set<Disable.Feature> disable() {
        Disable annotation = element.getAnnotation(Disable.class);
        return annotation != null ? new HashSet<>(Arrays.asList(annotation.value())) : Collections.emptySet();
    }

    public Optional<String> defaultValue() {
        return Optional.ofNullable(element.getAnnotation(Default.class)).map(a -> a.value());
    }

    public Optional<String> key() {
        return Optional.ofNullable(element.getAnnotation(Key.class)).map(a -> a.value());
    }

    public ConfigInfo config() {
        Config config = element.getAnnotation(Config.class);
        return new ConfigInfo(config.sources(), config.loadStrategy());
    }
}
