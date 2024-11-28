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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class HjsonToMapParserTest {

    @Test void correct() throws IOException {
        try (var reader = reader("correct.hjson")) {
            var result = new HjsonToMapParser().parse(reader);
            assertEquals("true", result.get("database.enabled"));
            assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454,123e4567-e89b-12d3-a456-556642440000", result.get("id"));
        }
    }

    @Test void multilineValue() throws IOException {
        try (var reader = reader("correct.hjson")) {
            var result = new HjsonToMapParser().parse(reader);
            var multiline = result.get("database.temp_targets.multiline");
            var lines = multiline.split("\r\n|\r|\n");
            assertEquals(3, lines.length);
            assertEquals("First line.", new BufferedReader(new StringReader(multiline)).readLine());
        }
    }

    @Test void empty() throws IOException {
        try (var reader = reader("empty.hjson")) {
            assertTrue(new HjsonToMapParser().parse(reader).isEmpty());
        }
    }

    @Test void wrongRoot() throws IOException {
        try (var reader = reader("wrongroot.hjson")) {
            var parser = new HjsonToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    @Test void wrong() throws IOException {
        try (var reader = reader("wrong.txt")) {
            var parser = new HjsonToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    @Test void arrayInArray() throws IOException {
        try (var reader = reader("arrayInArray.hjson")) {
            var parser = new HjsonToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    @Test void objectInArray() throws IOException {
        try (var reader = reader("objectInArray.hjson")) {
            var parser = new HjsonToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    private Reader reader(String resource) {
        var is = HjsonToMapParserTest.class.getClassLoader().getResourceAsStream(resource);
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
