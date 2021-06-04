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
package net.cactusthorn.config.extras.json.util;

import static net.cactusthorn.config.extras.json.util.JsonMessages.msg;
import static net.cactusthorn.config.extras.json.util.JsonMessages.Key.ROOT_OBJECT;
import static net.cactusthorn.config.extras.json.util.JsonMessages.Key.OBJECTS_IN_ARRAY;
import static net.cactusthorn.config.extras.json.util.JsonMessages.Key.ARRAYS_IN_ARRAY;

import java.io.IOException;
import java.io.Reader;

import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.Deque;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Collections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONToMapParser {

    public Map<String, String> parse(Reader reader) throws IOException {
        JsonElement rootElement = JsonParser.parseReader(reader);
        if (rootElement.isJsonNull()) {
            return Collections.emptyMap();
        }
        if (!rootElement.isJsonObject()) {
            throw new UnsupportedOperationException(msg(ROOT_OBJECT));
        }
        Map<String, String> result = new HashMap<>();
        processObjectElement(new ArrayDeque<>(), result, rootElement.getAsJsonObject());
        return Collections.unmodifiableMap(result);
    }

    private void processObjectElement(Deque<String> key, Map<String, String> result, JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                key.addLast(entry.getKey());
                processObjectElement(key, result, entry.getValue());
                key.removeLast();
            }
        } else if (element.isJsonArray()) {
            processArrayElement(key, result, element);
        } else if (element.isJsonPrimitive()) {
            result.put(key.stream().collect(Collectors.joining(".")), element.toString());
        }
    }

    private void processArrayElement(Deque<String> key, Map<String, String> result, JsonElement element) {
        JsonArray array = element.getAsJsonArray();
        String keyAsString = key.stream().collect(Collectors.joining("."));
        StringJoiner joiner = new StringJoiner(",");
        for (JsonElement arrayElement : array) {
            if (arrayElement.isJsonNull()) {
                continue;
            }
            if (arrayElement.isJsonObject()) {
                throw new UnsupportedOperationException(msg(OBJECTS_IN_ARRAY, keyAsString));
            }
            if (arrayElement.isJsonArray()) {
                throw new UnsupportedOperationException(msg(ARRAYS_IN_ARRAY, keyAsString));
            }
            joiner.add(arrayElement.getAsString());
        }
        result.put(keyAsString, joiner.toString());
    }
}
