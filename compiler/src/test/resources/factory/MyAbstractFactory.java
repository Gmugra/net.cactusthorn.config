package factory;

import test.SimpleConfig;

import net.cactusthorn.config.core.factory.Factory;

@Factory
public abstract class MyAbstractFactory {

    public abstract SimpleConfig createSimpleConfig();
}
