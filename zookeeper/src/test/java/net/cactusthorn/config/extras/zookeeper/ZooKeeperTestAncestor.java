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

import static net.cactusthorn.config.extras.zookeeper.util.ZNodeToMapParser.NEVER_RETRY_POLICY;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.ZKPaths;

public class ZooKeeperTestAncestor {

    protected static final String BASE_PATH = "/test";
    protected static final String THANKS_KEY = "thanks";
    protected static final String THANKS_VALUE = "welcome";
    protected static final String GREETINGS_KEY = "greetings";
    protected static final String GREETINGS_VALUE = "hi,bonjour,hiya,hi!";

    protected static TestingServer init(int port) throws Exception {
        TestingServer server = new TestingServer(port);
        server.start();
        try (CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), 1000, 1000, NEVER_RETRY_POLICY)) {
            client.start();
            client.blockUntilConnected();
            String basePath = BASE_PATH;
            setDataInZooKeeperServer(client, basePath, THANKS_KEY, THANKS_VALUE);
            setDataInZooKeeperServer(client, basePath, GREETINGS_KEY, GREETINGS_VALUE);
        }
        return server;
    }

    private static void setDataInZooKeeperServer(CuratorFramework client, String basePath, String property, String value) throws Exception {
        String path = ZKPaths.makePath(basePath, property);
        client.create().creatingParentsIfNeeded().forPath(path, value.getBytes());
    }

}
