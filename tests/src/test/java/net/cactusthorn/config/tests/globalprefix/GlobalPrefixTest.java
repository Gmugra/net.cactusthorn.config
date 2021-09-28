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
package net.cactusthorn.config.tests.globalprefix;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

public class GlobalPrefixTest {

    @Test public void disableMethod() {
        System.clearProperty("xxx");
        Map<String, String> properties = new HashMap<>();
        properties.put("value", "ABC");
        properties.put("gp.gpValue", "XYZ");
        GPDisableMethod config = ConfigFactory.builder().setSource(properties).setGlobalPrefix("gp").build().create(GPDisableMethod.class);
        assertEquals("ABC", config.value());
        assertEquals("XYZ", config.gpValue());
    }

    @Test public void disableMethodEnv() {
        System.setProperty("xxx", "dev");
        Map<String, String> properties = new HashMap<>();
        properties.put("value", "ABC");
        properties.put("gp.dev.gpValue", "XYZ");
        GPDisableMethod config = ConfigFactory.builder().setSource(properties).setGlobalPrefix("gp").build().create(GPDisableMethod.class);
        assertEquals("ABC", config.value());
        assertEquals("XYZ", config.gpValue());
    }

    @Test public void disableAll() {
        System.clearProperty("xxx");
        Map<String, String> properties = new HashMap<>();
        properties.put("value", "ABC");
        properties.put("gpValue", "XYZ");
        GPDisableAll config = ConfigFactory.builder().setSource(properties).setGlobalPrefix("gp").build().create(GPDisableAll.class);
        assertEquals("ABC", config.value());
        assertEquals("XYZ", config.gpValue());
    }

    @Test public void gpNotSet() {
        System.clearProperty("xxx");
        Map<String, String> properties = new HashMap<>();
        properties.put("value", "ABC");
        properties.put("gpValue", "XYZ");
        GPDisableMethod config = ConfigFactory.builder().setSource(properties).build().create(GPDisableMethod.class);
        assertEquals("ABC", config.value());
        assertEquals("XYZ", config.gpValue());
    }
}
