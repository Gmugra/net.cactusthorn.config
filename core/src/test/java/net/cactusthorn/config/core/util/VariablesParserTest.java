package net.cactusthorn.config.core.util;

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.WRONG_SOURCE_PARAM;
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

        assertEquals("aaaBcccC", new VariablesParser("a{AA}aa{1bbb1}ccc{2c2}").replace(values));
    }

    @Test public void replaceVaraibles2() {
        Map<String, String> values = new HashMap<>();
        values.put("1bbb1", "B");
        values.put("2c2", "C");

        assertEquals("yyBcccCxx", new VariablesParser("{AA}yy{1bbb1}ccc{2c2}xx").replace(values));
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
        String template = "a{a}a{fff{f}";
        Exception e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser(template).replace(Collections.emptyMap()));
        assertEquals(msg(WRONG_SOURCE_PARAM, 9, template), e.getMessage());
    }

    @Test public void wrongVarEnd() {
        String template = "aa{a}fff}f";
        Exception e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser(template).replace(Collections.emptyMap()));
        assertEquals(msg(WRONG_SOURCE_PARAM, template, 8), e.getMessage());
    }

    @Test public void wrongVarNoEnd() {
        String template = "a{a}aa{a}dd{fff";
        Exception e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser(template).replace(Collections.emptyMap()));
        assertEquals(msg(WRONG_SOURCE_PARAM, 11, template), e.getMessage());
    }
}
