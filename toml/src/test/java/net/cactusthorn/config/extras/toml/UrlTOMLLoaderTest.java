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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.cactusthorn.config.core.loader.Loader;

class UrlTOMLLoaderTest {

    private static final Loader LOADER = new UrlTOMLLoader();
    private static final ClassLoader CL = UrlTOMLLoaderTest.class.getClassLoader();

    @Test void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("file:./a.toml")));
    }

    @Test void notAcceptExtension() {
        assertFalse(LOADER.accept(URI.create("file:./a.properties")));
    }

    @Test void notAcceptException() {
        assertFalse(LOADER.accept(URI.create("github.com/a.toml")));
    }

    @Test void notAcceptException2() {
        assertFalse(LOADER.accept(URI.create("system:a.toml")));
    }

    @Test void load(@TempDir Path path) throws IOException {
        var file = path.resolve("correct.toml");
        try (var stream = CL.getResourceAsStream("correct.toml")) {
            Files.copy(stream, file);
        }
        var properties = LOADER.load(file.toUri(), CL);
        assertEquals("frontend", properties.get("servers.alpha.role"));
    }

    @Test void loadFragment(@TempDir Path path) throws IOException, URISyntaxException {
        var file = path.resolve("correct.toml");
        try (var stream = CL.getResourceAsStream("correct.toml")) {
            Files.copy(stream, file);
        }
        var uri = file.toUri();
        uri = new URI(uri.getScheme(), uri.getSchemeSpecificPart(), "UTF-8");
        var properties = LOADER.load(uri, CL);
        assertEquals("frontend", properties.get("servers.alpha.role"));
    }

    @Test void notLoad() throws IOException {
        var properties = LOADER.load(URI.create("file:./a.toml"), CL);
        assertTrue(properties.isEmpty());
    }
}
