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
package net.cactusthorn.config.tests.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;
import net.cactusthorn.config.core.loader.ReloadEvent;
import net.cactusthorn.config.core.loader.ReloadListener;

public class ListenerTest {

    private static final String PROPERTY_NAME = "value";

    private static String oldValue;
    private static String newValue;

    private static class MyListener implements ReloadListener {

        @Override public void reloadPerformed(ReloadEvent event) {
            oldValue = (String) event.oldProperties().get(PROPERTY_NAME);
            newValue = (String) event.newProperties().get(PROPERTY_NAME);
        }
    }

    @Test public void doIt() {
        oldValue = "000";
        newValue = "111";
        Map<String, String> props = new HashMap<>();
        props.put(PROPERTY_NAME, "ABC");

        MyConfig config = ConfigFactory.builder().setSource(props).build().create(MyConfig.class);
        assertEquals("ABC", config.value());
        assertEquals("000", oldValue);
        assertEquals("111", newValue);

        config.addReloadListener(new MyListener());
        props.put(PROPERTY_NAME, "XYZ");
        config.reload();
        assertEquals("XYZ", config.value());
        assertEquals("ABC", oldValue);
        assertEquals("XYZ", newValue);
    }
}
