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
package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReloadEventTest {

    private static final String PROPERTY_NAME = "value";

    private static ReloadEvent event;

    @BeforeAll static void setUp() {
        Map<String, Object> oldValues = new HashMap<>();
        oldValues.put(PROPERTY_NAME, "OLD");
        Map<String, Object> newValues = new HashMap<>();
        newValues.put(PROPERTY_NAME, "NEW");
        event = new ReloadEvent(new Object(), oldValues, newValues);
    }

    @Test public void oldValues() {
        assertEquals("OLD", event.oldProperties().get(PROPERTY_NAME));
        assertThrows(UnsupportedOperationException.class, () -> event.oldProperties().put("a", "b"));
    }

    @Test public void newValues() {
        assertEquals("NEW", event.newProperties().get(PROPERTY_NAME));
        assertThrows(UnsupportedOperationException.class, () -> event.newProperties().put("a", "b"));
    }
}
