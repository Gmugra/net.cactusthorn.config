/*
 * Copyright (C) 2021, Alexei Khatskevich
 *
 * Licensed under the BSD 3-Clause license.
 * You may obtain a copy of the License at
 *
 * https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.cactusthorn.config.core.util;

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.WRONG_SOURCE_PARAM;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

class VariablesParserTest {

    @Test void withoutVars() {
        assertEquals("withoutVars", new VariablesParser("withoutVars").replace(Collections.emptyMap()));
    }

    @Test void replaceVaraibles() {
        var values = Map.of("1bbb1", "B", "2c2", "C");
        assertEquals("aaaBcccC", new VariablesParser("a{AA}aa{1bbb1}ccc{2c2}").replace(values));
    }

    @Test void replaceVaraibles2() {
        var values = Map.of("1bbb1", "B", "2c2", "C");
        assertEquals("yyBcccCxx", new VariablesParser("{AA}yy{1bbb1}ccc{2c2}xx").replace(values));
    }

    @Test void sourceNull() {
        var e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser(null));
        assertEquals(isNull("source"), e.getMessage());
    }

    @Test void sourceEmpty() {
        var e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser("  "));
        assertEquals(isEmpty("source"), e.getMessage());
    }

    @Test void valuesNull() {
        var e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser("aaa").replace(null));
        assertEquals(isNull("values"), e.getMessage());
    }

    @Test void wrongVarStart() {
        var template = "a{a}a{fff{f}";
        var e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser(template).replace(Collections.emptyMap()));
        assertEquals(msg(WRONG_SOURCE_PARAM, 9, template), e.getMessage());
    }

    @Test void wrongVarEnd() {
        var template = "aa{a}fff}f";
        var e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser(template).replace(Collections.emptyMap()));
        assertEquals(msg(WRONG_SOURCE_PARAM, template, 8), e.getMessage());
    }

    @Test void wrongVarNoEnd() {
        var template = "a{a}aa{a}dd{fff";
        var e = assertThrows(IllegalArgumentException.class, () -> new VariablesParser(template).replace(Collections.emptyMap()));
        assertEquals(msg(WRONG_SOURCE_PARAM, 11, template), e.getMessage());
    }

    @Test void replaceVaraiblesWithDefault() {
        var values = Map.of("AA", "B", "2c2", "C");
        assertEquals("ByyDEFAULTcccCxx", new VariablesParser("{AA}yy{1bbb1:DEFAULT}ccc{2c2}xx").replace(values));
    }

    @Test void replaceVaraiblesWithDefaultSeparator() {
        var values = Map.of("AA", "B", "2c2", "C");
        assertEquals("ByycccCxx", new VariablesParser("{AA}yy{1bbb1:}ccc{2c2}xx").replace(values));
    }
}
