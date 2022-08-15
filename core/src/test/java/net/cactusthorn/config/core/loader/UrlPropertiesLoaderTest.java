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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.io.TempDir;

import net.cactusthorn.config.core.loader.standard.UrlPropertiesLoader;

public class UrlPropertiesLoaderTest {

    private static final Loader LOADER = new UrlPropertiesLoader();
    private static final ClassLoader CL = UrlPropertiesLoaderTest.class.getClassLoader();

    @Test public void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("file:./a.properties")));
    }

    @Test public void notAcceptExtension() {
        assertFalse(LOADER.accept(URI.create("file:./a.xml")));
    }

    @Test public void notAcceptException() {
        assertFalse(LOADER.accept(URI.create("github.com/a.properties")));
    }

    @Test public void notAcceptException2() {
        assertFalse(LOADER.accept(URI.create("system:a.xml")));
    }

    @Test public void load(@TempDir Path path) throws IOException {
        URI uri = prepareTempFile(path, "test.properties");
        Map<String, String> properties = LOADER.load(uri, CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Test public void loadFragment(@TempDir Path path) throws IOException, URISyntaxException {
        URI uri = prepareTempFile(path, "test.properties");
        uri = new URI(uri.getScheme(), uri.getSchemeSpecificPart(), "UTF-8");
        Map<String, String> properties = LOADER.load(uri, CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Disabled @Test public void loadGithub(@TempDir Path path) throws IOException, URISyntaxException {
        URI uri = URI.create(
                "https://raw.githubusercontent.com/Gmugra/net.cactusthorn.config/main/core/src/test/resources/test.properties#UTF-8");
        Map<String, String> properties = LOADER.load(uri, CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Test public void notLoad() throws IOException {
        Map<String, String> properties = LOADER.load(URI.create("file:./a.properties"), CL);
        assertTrue(properties.isEmpty());
    }

    @Test public void contentHashCodeNotFile() {
        long hashCode = LOADER.contentHashCode(URI.create("https://github.com/a.properties"), CL);
        assertEquals(0L, hashCode);
    }

    @Test public void contentHashCodeFileNotExist() {
        long hashCode = LOADER.contentHashCode(URI.create("file:./a.properties"), CL);
        assertEquals(0L, hashCode);
    }

    @Test public void contentHashCodeFile(@TempDir Path path) throws IOException, URISyntaxException {
        URI uri = prepareTempFile(path, "test.properties");
        long hashCode = LOADER.contentHashCode(uri, CL);
        assertTrue(hashCode > 0L);
    }

    @Test public void contentHashCodeFileFragment(@TempDir Path path) throws IOException, URISyntaxException {
        URI uri = prepareTempFile(path, "test.properties");
        uri = new URI(uri.getScheme(), uri.getSchemeSpecificPart(), "UTF-8");
        long hashCode = LOADER.contentHashCode(uri, CL);
        assertTrue(hashCode > 0L);
    }

    private URI prepareTempFile(Path tempDir, String fileName) throws IOException {
        Path file = tempDir.resolve(fileName);
        try (InputStream stream = CL.getResourceAsStream(fileName)) {
            Files.copy(stream, file);
        }
        return file.toUri();
    }
}
