package net.cactusthorn.config.core.configoverride;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class ConfigOverrideTest {

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }
    
    @Test public void first() {
        ConfigOverride conf = ConfigFactory.builder().addSource("system:properties").build().create(ConfigOverride.class);
        assertEquals("SECOND", conf.string());
    }
}
