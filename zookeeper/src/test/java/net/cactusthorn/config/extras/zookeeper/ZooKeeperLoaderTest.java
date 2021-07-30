package net.cactusthorn.config.extras.zookeeper;

import java.util.List;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.Loader;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class ZooKeeperLoaderTest extends ZooKeeperTestAncestor {

    private static final Loader LOADER = new ZooKeeperLoader();

    @BeforeAll public static void setupLog() throws Exception {
        // java.util.logging -> SLF4j
        org.slf4j.bridge.SLF4JBridgeHandler.removeHandlersForRootLogger();
        org.slf4j.bridge.SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.FINEST);
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
}
