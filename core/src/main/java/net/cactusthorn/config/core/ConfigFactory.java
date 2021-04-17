package net.cactusthorn.config.core;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;

public class ConfigFactory {

    private static final MethodType CONSTRUCTOR = MethodType.methodType(void.class, Map.class);

    @SuppressWarnings("unchecked") public static <T> T create(Class<T> sourceInterface, Map<String, String> properties) {
        try {
            Package interfacePackage = sourceInterface.getPackage();
            String interfaceName = sourceInterface.getSimpleName();
            String builderClassName = interfacePackage.getName() + '.' + ConfigBuilder.BUILDER_CLASSNAME_PREFIX + interfaceName;
            Class<?> builderClass = Class.forName(builderClassName);
            MethodHandle methodHandler = MethodHandles.publicLookup().findConstructor(builderClass, CONSTRUCTOR);
            @SuppressWarnings("rawtypes") ConfigBuilder builder = (ConfigBuilder) methodHandler.invoke(properties);
            return (T) builder.build();
        } catch (Throwable e) {
            throw new RuntimeException(e); //TODO Special Exception ?
        }
    }
}
