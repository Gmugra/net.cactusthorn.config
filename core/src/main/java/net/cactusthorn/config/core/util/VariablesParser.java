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
package net.cactusthorn.config.core.util;

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.WRONG_SOURCE_PARAM;

import java.util.Map;

public final class VariablesParser {

    private final String source;

    public VariablesParser(String source) {
        if (source == null) {
            throw new IllegalArgumentException(isNull("source"));
        }
        String prepared = source.trim();
        if (prepared.isEmpty()) {
            throw new IllegalArgumentException(isEmpty("source"));
        }
        this.source = prepared;
    }

    public String replace(final Map<String, String> values) {
        if (values == null) {
            throw new IllegalArgumentException(isNull("values"));
        }

        StringBuilder result = new StringBuilder();

        int pos = 0;
        int start = source.indexOf('{');
        while (start >= 0) {
            result.append(source.substring(pos, start));
            pos = source.indexOf('}', start + 1);
            if (pos == -1) {
                throw new IllegalArgumentException(msg(WRONG_SOURCE_PARAM, start, source));
            }
            String variable = source.substring(start + 1, pos);
            if (variable.indexOf('{') != -1) {
                throw new IllegalArgumentException(msg(WRONG_SOURCE_PARAM, start + 1 + variable.indexOf('{'), source));
            }
            String value = values.getOrDefault(variable, "");
            result.append(value);
            pos++;
            start = source.indexOf('{', pos);
        }
        result.append(source.substring(pos));
        if (result.indexOf("}") != -1) {
            throw new IllegalArgumentException(msg(WRONG_SOURCE_PARAM, source, source.indexOf("}", pos)));
        }
        return result.toString();
    }
}
