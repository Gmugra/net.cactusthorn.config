package net.cactusthorn.config.extras.zookeeper.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

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
                result.put(key, new String(client.getData().forPath(ZKPaths.makePath(basePath, key))));
            }
            return result;
        }
    }
}
