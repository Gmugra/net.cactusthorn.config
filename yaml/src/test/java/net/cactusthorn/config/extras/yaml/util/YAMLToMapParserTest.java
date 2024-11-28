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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class YAMLToMapParserTest {

    @Test void correct() throws IOException {
        try (var reader = reader("correct.yaml")) {
            var parser = new YAMLToMapParser();
            var result = parser.parse(reader);
            assertEquals("true", result.get("database.enabled"));
        }
    }

    @Test void wrong() throws IOException {
        try (var reader = reader("wrong.txt")) {
            assertThrows(Exception.class, () -> new YAMLToMapParser().parse(reader));
        }
    }

    @Test void empty() throws IOException {
        try (var reader = reader("empty.yaml")) {
            assertTrue(new YAMLToMapParser().parse(reader).isEmpty());
        }
    }

    @Test void arrayInArray() throws IOException {
        try (var reader = reader("arrayInArray.yaml")) {
            assertThrows(UnsupportedOperationException.class, () -> new YAMLToMapParser().parse(reader));
        }
    }

    @Test void mapInArray() throws IOException {
        try (var reader = reader("mapInArray.yaml")) {
            assertThrows(UnsupportedOperationException.class, () -> new YAMLToMapParser().parse(reader));
        }
    }

    @Test void multilineString() throws IOException {
        try (var reader = reader("correct.yaml")) {
            var result = new YAMLToMapParser().parse(reader);
            var lines = result.get("str1").split("\r\n|\r|\n");
            assertEquals(2, lines.length);
        }
    }

    private Reader reader(String resource) {
        var is = YAMLToMapParserTest.class.getClassLoader().getResourceAsStream(resource);
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
