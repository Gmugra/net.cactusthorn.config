package factory;

import net.cactusthorn.config.core.factory.Factory;

import test.SimpleConfig;

@Factory public interface MethodWithParameter {

    SimpleConfig createSimpleConfig(String param);
}
