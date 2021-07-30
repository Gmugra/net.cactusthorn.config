package net.cactusthorn.config.extras.zookeeper;

import java.util.List;

import net.cactusthorn.config.core.Config;

@Config public interface ZooKeeperConfig {

    String thanks();

    List<String> greetings();
}
