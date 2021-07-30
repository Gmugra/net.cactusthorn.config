package net.cactusthorn.config.extras.zookeeper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

public class ZooKeeperWithFactoryTest extends ZooKeeperTestAncestor {

    @Test public void doIt() {
        String uri = "zookeeper://";
        uri += server.getConnectString();
        uri += BASE_PATH;
        ZooKeeperConfig config = ConfigFactory.builder().addSource(uri).build().create(ZooKeeperConfig.class);
        assertEquals(THANKS_VALUE, config.thanks());
        assertEquals(4, config.greetings().size());
    }
}
