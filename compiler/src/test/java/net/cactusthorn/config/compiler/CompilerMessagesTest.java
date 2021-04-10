package net.cactusthorn.config.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import static net.cactusthorn.config.compiler.CompilerMessages.*;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CompilerMessagesTest {

    @BeforeAll static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test public void testIt() {
        assertEquals("Only interface can be annotated with @Config", msg(ONLY_INTERFACE));
    }
}
