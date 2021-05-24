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
package net.cactusthorn.config.extras.toml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TOMLToMapParserTest {

    @Test public void correct() throws IOException {
        try (Reader reader = reader("correct.toml")) {
            TOMLToMapParser parser = new TOMLToMapParser();
            Map<String, String> result = parser.parse(reader);
            assertEquals("frontend", result.get("servers.alpha.role"));
        }
    }

    @Test public void wrong() throws IOException {
        try (Reader reader = reader("wrong.toml")) {
            TOMLToMapParser parser = new TOMLToMapParser();
            assertThrows(IOException.class, () -> parser.parse(reader));
        }
    }

    @Test public void empty() throws IOException {
        try (Reader reader = reader("empty.toml")) {
            TOMLToMapParser parser = new TOMLToMapParser();
            assertTrue(parser.parse(reader).isEmpty());
        }
    }

    @Test public void arrayInArray() throws IOException {
        try (Reader reader = reader("arrayInArray.toml")) {
            TOMLToMapParser parser = new TOMLToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    @Test public void emptyArray() throws IOException {
        try (Reader reader = reader("emptyArray.toml")) {
            TOMLToMapParser parser = new TOMLToMapParser();
            assertTrue(parser.parse(reader).isEmpty());
        }
    }

    @Test public void tableInArray() throws IOException {
        try (Reader reader = reader("tableInArray.toml")) {
            TOMLToMapParser parser = new TOMLToMapParser();
            assertThrows(UnsupportedOperationException.class, () -> parser.parse(reader));
        }
    }

    private Reader reader(String resource) {
        InputStream is = TOMLToMapParserTest.class.getClassLoader().getResourceAsStream(resource);
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
