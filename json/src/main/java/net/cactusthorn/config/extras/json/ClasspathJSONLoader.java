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
package net.cactusthorn.config.extras.json;

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.CANT_LOAD_RESOURCE;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.extras.json.util.JSONToMapParser;

public class ClasspathJSONLoader implements Loader {

    private static final Logger LOG = Logger.getLogger(ClasspathJSONLoader.class.getName());

    private static final String SCHEME = "classpath";
    private static final String EXTENTION = ".json";

    private static final JSONToMapParser PARSER = new JSONToMapParser();

    @Override public boolean accept(URI uri) {
        return uri.isOpaque() && SCHEME.equals(uri.getScheme()) && uri.getSchemeSpecificPart().endsWith(EXTENTION);
    }

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
        String charsetName = uri.getFragment() == null ? StandardCharsets.UTF_8.name() : uri.getFragment();
        try (InputStream stream = classLoader.getResourceAsStream(uri.getSchemeSpecificPart());
                Reader reader = new InputStreamReader(stream, charsetName);
                BufferedReader buffer = new BufferedReader(reader)) {
            return PARSER.parse(reader);
        } catch (Exception e) {
            LOG.info(msg(CANT_LOAD_RESOURCE, uri.toString(), e.toString()));
            return Collections.emptyMap();
        }
    }
}
