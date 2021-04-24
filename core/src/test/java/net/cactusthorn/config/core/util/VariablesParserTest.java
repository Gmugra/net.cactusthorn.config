package net.cactusthorn.config.core.util;

import static net.cactusthorn.config.core.ApiMessages.*;
import static net.cactusthorn.config.core.ApiMessages.Key.WRONG_SOURCE_PARAM;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class VariablesParserTest {

    @Test public void withoutVars() {
        assertEquals("withoutVars", new VariablesParser("withoutVars").replace(Collections.emptyMap()));
    }

    @Test public void replaceVaraibles() {
        Map<String, String> values = new HashMap<>();
        values.put("1bbb1", "B");
        values.put("2c2", "C");

        assertEquals("aaaBcccC", new VariablesParser("a{AA}aa{1bbb1}ccc{ 2c2 }").replace(values));
    }

    @Test public void sourceNull() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser(null));
        assertEquals(isNull("source"), e.getMessage());
    }

    @Test public void sourceEmpty() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser("  "));
        assertEquals(isEmpty("source"), e.getMessage());
    }

    @Test public void valuesNull() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser("aaa").replace(null));
        assertEquals(isNull("values"), e.getMessage());
    }

    @Test public void wrongVarStart() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser("aaa{fff{f}").replace(Collections.emptyMap()));
        assertEquals(msg(WRONG_SOURCE_PARAM), e.getMessage());
    }

    @Test public void wrongVarNoEnd() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser("aaa{fff").replace(Collections.emptyMap()));
        assertEquals(msg(WRONG_SOURCE_PARAM), e.getMessage());
    }
}
