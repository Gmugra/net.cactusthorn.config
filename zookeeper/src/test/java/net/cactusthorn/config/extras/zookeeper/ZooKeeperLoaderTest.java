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

import java.util.List;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;
import net.cactusthorn.config.core.loader.Loader;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class ZooKeeperLoaderTest extends ZooKeeperTestAncestor {

    private static final Loader LOADER = new ZooKeeperLoader();

    private static TestingServer server;

    @BeforeAll public static void setupLog() throws Exception {
        server = init(8090);

        // java.util.logging -> SLF4j
        org.slf4j.bridge.SLF4JBridgeHandler.removeHandlersForRootLogger();
        org.slf4j.bridge.SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.FINEST);
    }

    @AfterAll public static void shutdown() throws Exception {
        server.stop();
    }

    @Test public void load() {
        String uri = "zookeeper://";
        uri += server.getConnectString();
        uri += BASE_PATH;
        uri += "?sessionTimeoutMs=10000&connectionTimeoutMs=2000";
        Map<String, String> properties = LOADER.load(URI.create(uri), ZooKeeperLoaderTest.class.getClassLoader());
        assertEquals(THANKS_VALUE, properties.get(THANKS_KEY));
        assertEquals(GREETINGS_VALUE, properties.get(GREETINGS_KEY));
    }

    @Test public void loadDefaultTimeouts() {
        String uri = "zookeeper://";
        uri += server.getConnectString();
        uri += BASE_PATH;
        Map<String, String> properties = LOADER.load(URI.create(uri), ZooKeeperLoaderTest.class.getClassLoader());
        assertEquals(THANKS_VALUE, properties.get(THANKS_KEY));
        assertEquals(GREETINGS_VALUE, properties.get(GREETINGS_KEY));
    }

    @Test public void wrongConnectString() {
        Logger logger = (Logger) LoggerFactory.getLogger(LOADER.getClass());
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        String uri = "zookeeper://localhost:65405" + BASE_PATH + "?blockUntilConnectedMaxWaitTimeMs=100";
        LOADER.load(URI.create(uri), ZooKeeperLoaderTest.class.getClassLoader());

        List<ILoggingEvent> logEvents = appender.list;
        assertTrue(logEvents.get(0).getFormattedMessage().startsWith("Can't load resource " + uri));
    }

    @Test public void fullLoad() {
        String uri = "zookeeper://";
        uri += server.getConnectString();
        uri += BASE_PATH;
        ZooKeeperConfig config = ConfigFactory.builder().addSource(uri).build().create(ZooKeeperConfig.class);
        assertEquals(THANKS_VALUE, config.thanks());
        assertEquals(4, config.greetings().size());
    }
}
