package net.cactusthorn.config.extras.zookeeper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.extras.zookeeper.ZooKeeperTestAncestor;

public class ZNodeToMapParserTest extends ZooKeeperTestAncestor {

    @Test public void parse() throws Exception {
        ZNodeToMapParser parser = new ZNodeToMapParser();
        Map<String, String> result = parser.parse(server.getConnectString(), 50, 50, 100, BASE_PATH);
        assertEquals(THANKS_VALUE, result.get(THANKS_KEY));
        assertEquals(GREETINGS_VALUE, result.get(GREETINGS_KEY));
    }
}
