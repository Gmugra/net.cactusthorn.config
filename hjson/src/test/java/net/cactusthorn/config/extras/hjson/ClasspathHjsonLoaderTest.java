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
package net.cactusthorn.config.extras.hjson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.Loader;

public class ClasspathHjsonLoaderTest {

    private static final Loader LOADER = new ClasspathHjsonLoader();
    private static final ClassLoader CL = ClasspathHjsonLoaderTest.class.getClassLoader();

    @Test public void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("classpath:standard-properties.hjson")));
    }

    @Test public void acceptFragment() {
        assertTrue(LOADER.accept(URI.create("classpath:standard-properties.hjson#ISO-8859-1")));
    }

    @Test public void notAcceptNotOpaque() {
        assertFalse(LOADER.accept(URI.create("classpath://a.hjson#ISO-8859-1")));
    }

    @Test public void notAcceptNotHjson() {
        assertFalse(LOADER.accept(URI.create("classpath:a.properties#ISO-8859-1")));
    }

    @Test public void notAcceptScheme() {
        assertFalse(LOADER.accept(URI.create("mail:a.hjson#ISO-8859-1")));
    }

    @Test public void load() {
        Map<String, String> properties = LOADER.load(URI.create("classpath:correct.hjson#UTF-8"), CL);
        assertEquals("true", properties.get("database.enabled"));
    }

    @Test public void loadWrong() {
        Map<String, String> properties = LOADER.load(URI.create("classpath:standard-properties-wrong.hjson"), CL);
        assertTrue(properties.isEmpty());
    }
}
