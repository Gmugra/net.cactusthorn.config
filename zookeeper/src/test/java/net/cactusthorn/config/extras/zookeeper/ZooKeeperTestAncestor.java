package net.cactusthorn.config.extras.zookeeper;

import static net.cactusthorn.config.extras.zookeeper.util.ZNodeToMapParser.NEVER_RETRY_POLICY;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.ZKPaths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class ZooKeeperTestAncestor {

    protected static final String BASE_PATH = "/test";
    protected static final String THANKS_KEY = "thanks";
    protected static final String THANKS_VALUE = "welcome";
    protected static final String GREETINGS_KEY = "greetings";
    protected static final String GREETINGS_VALUE = "hi,bonjour,hiya,hi!";

    protected static TestingServer server;

    @BeforeAll public static void setup() throws Exception {
        server = new TestingServer(65403);
        server.start();
        try (CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), 50, 50, NEVER_RETRY_POLICY)) {
            client.start();
            client.blockUntilConnected();
            String basePath = BASE_PATH;
            setDataInZookeperServer(client, basePath, THANKS_KEY, THANKS_VALUE);
            setDataInZookeperServer(client, basePath, GREETINGS_KEY, GREETINGS_VALUE);
        }
    }

    @AfterAll public static void shutdown() throws Exception {
        server.stop();
    }

    private static void setDataInZookeperServer(CuratorFramework client, String basePath, String property, String value) throws Exception {
        String path = ZKPaths.makePath(basePath, property);
        client.create().creatingParentsIfNeeded().forPath(path, value.getBytes());
    }

}
