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
package net.cactusthorn.config.extras.hjson.util;

import static net.cactusthorn.config.extras.hjson.util.HjsonMessages.msg;
import static net.cactusthorn.config.extras.hjson.util.HjsonMessages.Key.ROOT_OBJECT;
import static net.cactusthorn.config.extras.hjson.util.HjsonMessages.Key.OBJECTS_IN_ARRAY;
import static net.cactusthorn.config.extras.hjson.util.HjsonMessages.Key.ARRAYS_IN_ARRAY;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.hjson.Stringify;
import org.hjson.JsonArray;

public class HjsonToMapParser {

    public Map<String, String> parse(Reader reader) throws IOException {
        JsonValue jsonValue = JsonValue.readHjson(reader);
        if (!jsonValue.isObject()) {
            throw new UnsupportedOperationException(msg(ROOT_OBJECT));
        }
        JsonObject jsonObject = jsonValue.asObject();
        if (jsonObject.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        processObjectElement(new ArrayDeque<>(), result, jsonObject);
        return Collections.unmodifiableMap(result);
    }

    private void processObjectElement(Deque<String> key, Map<String, String> result, JsonValue jsonValue) {
        if (jsonValue.isObject()) {
            for (JsonObject.Member member : jsonValue.asObject()) {
                key.addLast(member.getName());
                processObjectElement(key, result, member.getValue());
                key.removeLast();
            }
        } else if (jsonValue.isArray()) {
            processArrayElement(key, result, jsonValue);
        } else if (!jsonValue.isNull()) {
            result.put(key.stream().collect(Collectors.joining(".")), jsonValue.toString(Stringify.HJSON));
        }
    }

    private void processArrayElement(Deque<String> key, Map<String, String> result, JsonValue jsonValue) {
        JsonArray array = jsonValue.asArray();
        String keyAsString = key.stream().collect(Collectors.joining("."));
        StringJoiner joiner = new StringJoiner(",");
        for (JsonValue arrayValue : array) {
            if (arrayValue.isNull()) {
                continue;
            }
            if (arrayValue.isObject()) {
                throw new UnsupportedOperationException(msg(OBJECTS_IN_ARRAY, keyAsString));
            }
            if (arrayValue.isArray()) {
                throw new UnsupportedOperationException(msg(ARRAYS_IN_ARRAY, keyAsString));
            }
            joiner.add(arrayValue.toString(Stringify.HJSON));
        }
        result.put(keyAsString, joiner.toString());
    }
}
