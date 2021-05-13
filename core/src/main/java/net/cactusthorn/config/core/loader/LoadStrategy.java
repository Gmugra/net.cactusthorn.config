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
package net.cactusthorn.config.core.loader;

import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public enum LoadStrategy {

    // @formatter:off
    FIRST(l -> first(new HashMap<>(), l)),
    MERGE(l -> merge(new HashMap<>(), l)),
    FIRST_KEYCASEINSENSITIVE(l -> first(new TreeMap<>(String.CASE_INSENSITIVE_ORDER), l)),
    MERGE_KEYCASEINSENSITIVE(l -> merge(new TreeMap<>(String.CASE_INSENSITIVE_ORDER), l)),
    UNKNOWN(l -> {
        throw new UnsupportedOperationException();
    });
    // @formatter:on

    private final Function<List<Map<String, String>>, Map<String, String>> strategy;

    LoadStrategy(Function<List<Map<String, String>>, Map<String, String>> strategy) {
        this.strategy = strategy;
    }

    public Map<String, String> combine(List<Map<String, String>> properties, Map<String, String> manualProperties) {
        Map<String, String> result = strategy.apply(properties);
        result.putAll(manualProperties); // Map with properties is always has highest priority
        return Collections.unmodifiableMap(result);
    }

    private static Map<String, String> first(Map<String, String> result, List<Map<String, String>> list) {
        for (Map<String, String> uriProperties : list) {
            if (!uriProperties.isEmpty()) {
                result.putAll(uriProperties);
                break;
            }
        }
        return result;
    }

    private static Map<String, String> merge(Map<String, String> result, List<Map<String, String>> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            result.putAll(list.get(i));
        }
        return result;
    }
}
