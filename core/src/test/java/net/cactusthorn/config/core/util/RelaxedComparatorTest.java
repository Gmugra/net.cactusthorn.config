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

import static org.junit.jupiter.api.Assertions.*;
import static net.cactusthorn.config.core.util.RelaxedComparator.RELAXED_ORDER;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class RelaxedComparatorTest {

    @Test void bothNull() {
        assertEquals(0, RELAXED_ORDER.compare(null, null));
    }

    @Test void key1Null() {
        assertEquals(1, RELAXED_ORDER.compare(null, ""));
    }

    @Test void key2Null() {
        assertEquals(-1, RELAXED_ORDER.compare("", null));
    }

    @Test void same() {
        assertEquals(0, RELAXED_ORDER.compare("person.first-name", "person.first-name"));
    }

    @Test void check1() {
        assertEquals(0, RELAXED_ORDER.compare("person.first-name", "person.firstName"));
    }

    @Test void check2() {
        assertEquals(0, RELAXED_ORDER.compare("PERSON_FIRSTNAME", "person.firstName"));
    }

    @Test void check3() {
        assertEquals(0, RELAXED_ORDER.compare("PERSON_FIRSTNAME", "person...first_-_Name"));
    }

    @Test void notSame() {
        assertFalse(RELAXED_ORDER.compare("PERSON_FIRSTNAME", "person.first") == 0);
    }

    @Test void map() {
        var m = new TreeMap<String, String>(RELAXED_ORDER);
        m.put("person.firstName", "A");
        m.put("PERSON_FIRSTNAME", "B");
        assertEquals(1, m.size());
        assertEquals("B", m.get("person.firstName"));
        assertEquals("B", m.get("person.first-name"));
    }

}
