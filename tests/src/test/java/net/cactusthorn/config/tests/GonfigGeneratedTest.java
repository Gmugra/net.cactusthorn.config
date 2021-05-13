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
package net.cactusthorn.config.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GonfigGeneratedTest {

    private static AllCorrect config;
    private static AllCorrect config2;

    @BeforeAll static void setUp() {
        Map<String, Object> v1 = new HashMap<>();
        v1.put("buf", new StringBuilder("ABC"));
        v1.put("ddd", 125d);
        v1.put("fromStringEnum", AllCorrect.FromStringEnum.BBB);
        v1.put("intValue", 124);
        v1.put("list", Optional.empty());
        v1.put("set", new HashSet<>(Arrays.asList(UUID.fromString("46400000-8cc0-11bd-b43e-10d46e4ef14d"))));
        v1.put("simpleEnum", AllCorrect.SimpleEnum.AAA);
        v1.put("sorted", new TreeSet<>(Arrays.asList(126f)));
        v1.put("superInterface", "SI");
        v1.put("uuid", Optional.of(UUID.fromString("46400000-8cc0-11bd-b43e-10d46e4ef14d")));
        v1.put("value", "simpleString");
        v1.put("myChar", 'Y');
        config = new Config_AllCorrect(v1);
        config2 = new Config_AllCorrect(v1);
    }

    @Test public void toStr() {
        assertEquals(
                "[buf=ABC, ddd=125.0, fromStringEnum=BBB, intValue=124, list=Optional.empty, myChar=Y, set=[46400000-8cc0-11bd-b43e-10d46e4ef14d], simpleEnum=AAA, sorted=[126.0], superInterface=SI, uuid=Optional[46400000-8cc0-11bd-b43e-10d46e4ef14d], value=simpleString]",
                config.toString());
    }

    @Test public void eqls() {
        assertEquals(config, config);
    }

    @Test public void hash() {
        assertEquals(config.hashCode(), config2.hashCode());
    }

    @Test public void buf() {
        assertEquals("ABC", config.buf().toString());
    }

    @Test public void ddd() {
        assertEquals(125d, config.ddd());
    }

    @Test public void fromStringEnum() {
        assertEquals(AllCorrect.FromStringEnum.BBB, config.fromStringEnum());
    }

    @Test public void intValue() {
        assertEquals(124, config.intValue());
    }

    @Test public void list() {
        assertFalse(config.list().isPresent());
    }

    @Test public void set() {
        assertEquals(1, config.set().size());
        assertEquals("46400000-8cc0-11bd-b43e-10d46e4ef14d", config.set().iterator().next().toString());
    }

    @Test public void simpleEnum() {
        assertEquals(AllCorrect.SimpleEnum.AAA, config.simpleEnum());
    }

    @Test public void sorted() {
        assertEquals(1, config.sorted().size());
        assertEquals(126f, config.sorted().iterator().next());
    }

    @Test public void superInterface() {
        assertEquals("SI", config.superInterface());
    }

    @Test public void uuid() {
        assertEquals("46400000-8cc0-11bd-b43e-10d46e4ef14d", config.uuid().get().toString());
    }

    @Test public void value() {
        assertEquals("simpleString", config.value());
    }
}
