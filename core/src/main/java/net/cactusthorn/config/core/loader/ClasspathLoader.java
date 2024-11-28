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

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.CANT_LOAD_RESOURCE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClasspathLoader implements Loader {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected static final String CLASSPATH_SCHEME = "classpath";

    protected boolean accept(URI uri, String extension) {
        return uri.isOpaque() && CLASSPATH_SCHEME.equals(uri.getScheme()) && uri.getSchemeSpecificPart().endsWith(extension);
    }

    protected abstract Map<String, String> load(Reader reader) throws Exception;

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
        var charsetName = uri.getFragment() == null ? StandardCharsets.UTF_8.name() : uri.getFragment();
        try (var stream = classLoader.getResourceAsStream(uri.getSchemeSpecificPart());
                var reader = new InputStreamReader(stream, charsetName);
                var buffer = new BufferedReader(reader)) {
            return load(buffer);
        } catch (Exception e) {
            log.info(msg(CANT_LOAD_RESOURCE, uri.toString(), e.toString()));
            return Collections.emptyMap();
        }
    }
}
