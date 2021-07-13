package factory;

import test.SimpleConfig;

import net.cactusthorn.config.core.factory.Factory;

@Factory
public interface MyFactory {

    String CONST = "A";

    SimpleConfig createSimpleConfig();
}
