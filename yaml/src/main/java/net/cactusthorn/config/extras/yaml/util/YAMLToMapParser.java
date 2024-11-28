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
package net.cactusthorn.config.extras.yaml.util;

import static net.cactusthorn.config.extras.yaml.util.YamlMessages.msg;
import static net.cactusthorn.config.extras.yaml.util.YamlMessages.Key.ARRAYS_IN_ARRAY;
import static net.cactusthorn.config.extras.yaml.util.YamlMessages.Key.MAP_IN_ARRAY;

import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

public class YAMLToMapParser {

    public Map<String, String> parse(Reader reader) {
        var yaml = new Yaml();
        Map<String, Object> mapping = yaml.load(reader);
        if (mapping == null) {
            return Collections.emptyMap();
        }
        var result = new HashMap<String, String>();
        processMap(new ArrayDeque<>(), result, mapping);
        return Collections.unmodifiableMap(result);
    }

    @SuppressWarnings("unchecked") private void processMap(Deque<String> key, Map<String, String> result, Map<String, Object> map) {
        for (var entry : map.entrySet()) {
            var value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                key.addLast(entry.getKey());
                processMap(key, result, (Map<String, Object>) value);
                key.removeLast();
            } else if (value instanceof List<?>) {
                key.addLast(entry.getKey());
                processList(key, result, (List<?>) value);
                key.removeLast();
            } else {
                key.addLast(entry.getKey());
                result.put(key.stream().collect(Collectors.joining(".")), value.toString());
                key.removeLast();
            }
        }
    }

    private void processList(Deque<String> key, Map<String, String> result, List<?> list) {
        var keyAsString = key.stream().collect(Collectors.joining("."));
        var joiner = new StringJoiner(",");
        for (var value : list) {
            if (value == null) {
                continue;
            }
            if (value instanceof Map<?, ?>) {
                throw new UnsupportedOperationException(msg(MAP_IN_ARRAY, keyAsString));
            } else if (value instanceof List<?>) {
                throw new UnsupportedOperationException(msg(ARRAYS_IN_ARRAY, keyAsString));
            }
            joiner.add(value.toString());
        }
        result.put(keyAsString, joiner.toString());
    }
}
