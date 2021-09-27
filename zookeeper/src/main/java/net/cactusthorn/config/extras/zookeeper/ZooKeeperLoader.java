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
package net.cactusthorn.config.extras.zookeeper;

import static net.cactusthorn.config.core.util.ApiMessages.msg;
import static net.cactusthorn.config.core.util.ApiMessages.Key.CANT_LOAD_RESOURCE;

import java.net.URI;
import java.util.Collections;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.extras.zookeeper.util.ZNodeToMapParser;

public class ZooKeeperLoader implements Loader {

    private static final Logger LOG = Logger.getLogger(ZooKeeperLoader.class.getName());

    private static final String SCHEME = "zookeeper";

    private static final ZNodeToMapParser PARSER = new ZNodeToMapParser();

    private static final String SESSION_TIMEOUT_MS_PARAM = "sessionTimeoutMs";
    private static final String CONNECTION_TIMEOUT_MS_PARAM = "connectionTimeoutMs";
    private static final String BUC_MAX_WAIT_TIME_MS_PARAM = "blockUntilConnectedMaxWaitTimeMs";
    private static final String SESSION_TIMEOUT_DEAFAULT = "5000";
    private static final String CONNECTION_TIMEOUT_DEFAULT = "1000";
    private static final String BUC_MAX_WAIT_TIME_DEFAULT = "30000";

    /**
     * Example: "zookeeper://localhost:7777,localhost:8888/basePath
     */
    @Override public boolean accept(URI uri) {
        return !uri.isOpaque() && SCHEME.equals(uri.getScheme()) && !stringIsNullOrEmpty(uri.getAuthority())
                && !stringIsNullOrEmpty(uri.getPath());
    }

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {
        try {
            String connectString = uri.getAuthority();
            String basePath = uri.getPath();
            Map<String, String> parameters = parseQuery(uri.getQuery());
            int sessionTimeoutMs = Integer.parseInt(parameters.getOrDefault(SESSION_TIMEOUT_MS_PARAM, SESSION_TIMEOUT_DEAFAULT));
            int connectionTimeoutMs = Integer.parseInt(parameters.getOrDefault(CONNECTION_TIMEOUT_MS_PARAM, CONNECTION_TIMEOUT_DEFAULT));
            int bucMaxWaitTimeMs = Integer.parseInt(parameters.getOrDefault(BUC_MAX_WAIT_TIME_MS_PARAM, BUC_MAX_WAIT_TIME_DEFAULT));
            return PARSER.parse(connectString, sessionTimeoutMs, connectionTimeoutMs, bucMaxWaitTimeMs, basePath);
        } catch (Exception e) {
            LOG.info(msg(CANT_LOAD_RESOURCE, uri.toString(), e.toString()));
            return Collections.emptyMap();
        }
    }

    private boolean stringIsNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private Map<String, String> parseQuery(String query) {
        if (stringIsNullOrEmpty(query)) {
            return Collections.emptyMap();
        }
        return Arrays.stream(query.split("&")).map(s -> s.split("=")).collect(Collectors.toMap(a -> a[0], a -> a[1]));
    }
}
