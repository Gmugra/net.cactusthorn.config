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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class HjsonToMapParserTest {

    @Test public void correct() throws IOException {
        try (Reader reader = reader("correct.hjson")) {
            Map<String, String> result = new HjsonToMapParser().parse(reader);
            assertEquals("true", result.get("database.enabled"));
            assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454,123e4567-e89b-12d3-a456-556642440000", result.get("id"));
        }
    }

    @Test public void multilineValue() throws IOException {
        try (Reader reader = reader("correct.hjson")) {
            Map<String, String> result = new HjsonToMapParser().parse(reader);
            String multiline = result.get("database.temp_targets.multiline");
            String[] lines = multiline.split("\r\n|\r|\n");
            assertEquals(3, lines.length);
            assertEquals("First line.", new BufferedReader(new StringReader(multiline)).readLine());
        }
    }

    @Test public void empty() throws IOException {
        try (Reader reader = reader("empty.hjson")) {
            assertTrue(new HjsonToMapParser().parse(reader).isEmpty());
        }
    }

    @Test public void wrongRoot() throws IOException {
        try (Reader reader = reader("wrongroot.hjson")) {
            HjsonToMapParser parser = new HjsonToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    @Test public void wrong() throws IOException {
        try (Reader reader = reader("wrong.txt")) {
            HjsonToMapParser parser = new HjsonToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    @Test public void arrayInArray() throws IOException {
        try (Reader reader = reader("arrayInArray.hjson")) {
            HjsonToMapParser parser = new HjsonToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    @Test public void objectInArray() throws IOException {
        try (Reader reader = reader("objectInArray.hjson")) {
            HjsonToMapParser parser = new HjsonToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    private Reader reader(String resource) {
        InputStream is = HjsonToMapParserTest.class.getClassLoader().getResourceAsStream(resource);
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
