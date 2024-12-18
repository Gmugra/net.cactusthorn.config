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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

class GonfigBuilderGeneratedTest {

    private static AllCorrect config;

    @BeforeAll static void setUp() {
        var properties = new HashMap<String, String>();
        properties.put("ddd", "125");
        properties.put("fromStringEnum", "xyz");
        properties.put("intValue", "124");
        // properties.put("list", "?"); is empty
        properties.put("set", "46400000-8cc0-11bd-b43e-10d46e4ef14d");
        properties.put("simpleEnum", "AAA");
        properties.put("sorted", "126,300");
        properties.put("superInterface", "SI");
        properties.put("uuid", "46400000-8cc0-11bd-b43e-10d46e4ef14d");
        properties.put("value", "simpleString");
        properties.put("myChar", "YXZ");

        config = ConfigFactory.builder().setSource(properties).build().create(AllCorrect.class);
    }

    @Test void ddd() {
        assertEquals(125d, config.ddd());
    }

    @Test void fromStringEnum() {
        assertEquals(AllCorrect.FromStringEnum.AAA, config.fromStringEnum());
    }

    @Test void intValue() {
        assertEquals(124, config.intValue());
    }

    @Test void list() {
        assertTrue(config.list().isEmpty());
    }

    @Test void set() {
        assertEquals(1, config.set().size());
        assertEquals(UUID.fromString("46400000-8cc0-11bd-b43e-10d46e4ef14d"), config.set().iterator().next());
    }

    @Test void simpleEnum() {
        assertEquals(AllCorrect.SimpleEnum.AAA, config.simpleEnum());
    }

    @Test void sorted() {
        assertEquals(2, config.sorted().size());
        var it = config.sorted().iterator();
        assertEquals(126f, it.next());
        assertEquals(300f, it.next());
    }

    @Test void uuid() {
        assertEquals(Optional.of(UUID.fromString("46400000-8cc0-11bd-b43e-10d46e4ef14d")), config.uuid());
    }

    @Test void value() {
        assertEquals("simpleString", config.value());
    }
}
