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

    public static final class ConfigInfo {
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
