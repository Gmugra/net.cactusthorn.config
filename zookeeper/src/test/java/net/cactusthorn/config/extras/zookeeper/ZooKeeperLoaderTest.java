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

import java.net.URI;

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
    }

    @AfterAll public static void shutdown() throws Exception {
        server.stop();
    }

    @Test public void load() {
        var uri = "zookeeper://" + server.getConnectString() + BASE_PATH;
        uri += "?sessionTimeoutMs=10000&connectionTimeoutMs=2000";
        var properties = LOADER.load(URI.create(uri), ZooKeeperLoaderTest.class.getClassLoader());
        assertEquals(THANKS_VALUE, properties.get(THANKS_KEY));
        assertEquals(GREETINGS_VALUE, properties.get(GREETINGS_KEY));
    }

    @Test public void loadDefaultTimeouts() {
        var uri = "zookeeper://" + server.getConnectString() + BASE_PATH;
        var properties = LOADER.load(URI.create(uri), ZooKeeperLoaderTest.class.getClassLoader());
        assertEquals(THANKS_VALUE, properties.get(THANKS_KEY));
        assertEquals(GREETINGS_VALUE, properties.get(GREETINGS_KEY));
    }

    @Test public void wrongConnectString() {
        var logger = (Logger) LoggerFactory.getLogger(LOADER.getClass());
        var appender = new ListAppender<ILoggingEvent>();
        appender.start();
        logger.addAppender(appender);

        var uri = "zookeeper://localhost:65405" + BASE_PATH + "?blockUntilConnectedMaxWaitTimeMs=100";
        LOADER.load(URI.create(uri), ZooKeeperLoaderTest.class.getClassLoader());

        var logEvents = appender.list;
        assertTrue(logEvents.get(0).getFormattedMessage().startsWith("Can't load resource " + uri));
    }

    @Test public void fullLoad() {
        var uri = "zookeeper://";
        uri += server.getConnectString();
        uri += BASE_PATH;
        var config = ConfigFactory.builder().addSource(uri).build().create(ZooKeeperConfig.class);
        assertEquals(THANKS_VALUE, config.thanks());
        assertEquals(4, config.greetings().size());
    }
}
