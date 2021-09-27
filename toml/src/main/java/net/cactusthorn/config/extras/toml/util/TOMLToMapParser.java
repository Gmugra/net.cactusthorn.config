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
package net.cactusthorn.config.extras.toml.util;

import static net.cactusthorn.config.extras.toml.util.TomlMessages.msg;
import static net.cactusthorn.config.extras.toml.util.TomlMessages.Key.ARRAYS_IN_ARRAY;
import static net.cactusthorn.config.extras.toml.util.TomlMessages.Key.TABLES_IN_ARRAY;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;

public final class TOMLToMapParser {

    public Map<String, String> parse(Reader reader) throws IOException {
        TomlParseResult tomlResult = Toml.parse(reader);
        if (!tomlResult.errors().isEmpty()) {
            throw new IOException(tomlResult.errors().toString());
        }
        if (tomlResult.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        for (String key : tomlResult.dottedKeySet()) {
            Object value = tomlResult.get(key);
            if (value instanceof TomlArray) {
                String array = convertArray(key, (TomlArray) value);
                if (array != null) {
                    result.put(key, array);
                }
            } else {
                result.put(key, value.toString());
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private String convertArray(String key, TomlArray tomlArray) {
        if (tomlArray.isEmpty()) {
            return null;
        }
        if (tomlArray.containsArrays()) {
            throw new UnsupportedOperationException(msg(ARRAYS_IN_ARRAY, key));
        }
        if (tomlArray.containsTables()) {
            throw new UnsupportedOperationException(msg(TABLES_IN_ARRAY, key));
        }
        return tomlArray.toList().stream().map(o -> o.toString()).collect(Collectors.joining(","));
    }
}
