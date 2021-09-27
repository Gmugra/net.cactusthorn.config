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
package net.cactusthorn.config.extras.zookeeper.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.utils.ZKPaths;

public class ZNodeToMapParser {

    public static final RetryPolicy NEVER_RETRY_POLICY = (retryCount, elapsedTimeMs, sleeper) -> {
        return false;
    };

    public Map<String, String> parse(String connectString, int sessionTimeoutMs, int connectionTimeoutMs,
            int blockUntilConnectedMaxWaitTimeMs, String basePath) throws Exception {
        try (CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs,
                NEVER_RETRY_POLICY)) {
            client.start();
            client.blockUntilConnected(blockUntilConnectedMaxWaitTimeMs, MILLISECONDS);
            Map<String, String> result = new HashMap<>();
            for (String key : client.getChildren().forPath(basePath)) {
                result.put(key, new String(client.getData().forPath(ZKPaths.makePath(basePath, key)), StandardCharsets.UTF_8));
            }
            return result;
        }
    }
}
