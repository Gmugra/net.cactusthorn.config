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

import java.net.URI;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.standard.ClasspathJarManifestLoader;

class ClasspathJarManifestLoaderTest {

    private static final Loader LOADER = new ClasspathJarManifestLoader();
    private static final ClassLoader CL = ClasspathJarManifestLoaderTest.class.getClassLoader();

    @Test void accept() {
        assertTrue(LOADER.accept(URI.create("classpath:jar:manifest?a=b")));
    }

    @Test void notAccept() {
        assertFalse(LOADER.accept(URI.create("classpath:a.xml#ISO-8859-1")));
    }

    @Test void notFoundNameValue() {
        LOADER.load(URI.create("classpath:jar:manifest?a=b"), CL);
    }

    @Test void notFoundName() {
        LOADER.load(URI.create("classpath:jar:manifest?a"), CL);
    }

    @Test void load() {
        var result = LOADER.load(URI.create("classpath:jar:manifest?Bundle-Name=JUnit%20Jupiter%20API"), CL);
        assertEquals("junit-jupiter-api", result.get("Implementation-Title"));
    }

    @Test void loadOnlyName() {
        var result = LOADER.load(URI.create("classpath:jar:manifest?Bundle-Name"), CL);
        assertFalse(result.isEmpty());
    }
}
