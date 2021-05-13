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
