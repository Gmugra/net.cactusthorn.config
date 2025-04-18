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
import java.net.URI;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.standard.ClasspathPropertiesLoader;

class ClasspathPropertiesLoaderTest {

    private static final Loader LOADER = new ClasspathPropertiesLoader();
    private static final ClassLoader CL = ClasspathPropertiesLoaderTest.class.getClassLoader();

    @Test void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("classpath:a.properties")));
    }

    @Test void acceptFragment() {
        assertTrue(LOADER.accept(URI.create("classpath:a.properties#ISO-8859-1")));
    }

    @Test void notAcceptNotOpaque() {
        assertFalse(LOADER.accept(URI.create("classpath://a.properties#ISO-8859-1")));
    }

    @Test void notAcceptNotProperties() {
        assertFalse(LOADER.accept(URI.create("classpath:a.xml#ISO-8859-1")));
    }

    @Test void notAcceptScheme() {
        assertFalse(LOADER.accept(URI.create("mail:a.xml#ISO-8859-1")));
    }

    @Test void load() throws IOException {
        var properties = LOADER.load(URI.create("classpath:test.properties"), CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Test void loadWithFragment() throws IOException {
        var properties = LOADER.load(URI.create("classpath:test.properties#UTF-8"), CL);
        assertEquals("bbb", properties.get("aaa"));
    }

    @Test void notLoad() throws IOException {
        var properties = LOADER.load(URI.create("classpath:notExists.properties"), CL);
        assertTrue(properties.isEmpty());
    }
}
