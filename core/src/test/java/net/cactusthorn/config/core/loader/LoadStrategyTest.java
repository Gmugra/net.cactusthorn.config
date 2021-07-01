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
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;
import net.cactusthorn.config.core.TestConfig;

public class LoadStrategyTest {

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void merge() {
        TestConfig testConfig = ConfigFactory.builder()
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").build()
                .create(TestConfig.class);
        assertEquals("SSSSSSSS", testConfig.dstr());
    }

    @Test public void first() {
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").build()
                .create(TestConfig.class);
        assertEquals("A", testConfig.dstr());
    }

    @Test public void withMapMerge() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.dstr", "FROMMAP");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.MERGE)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").setSource(properties)
                .build().create(TestConfig.class);
        assertEquals("FROMMAP", testConfig.dstr());
    }

    @Test public void withMapFirst() {
        Map<String, String> properties = new HashMap<>();
        properties.put("test.dstr", "FROMMAP");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").setSource(properties)
                .build().create(TestConfig.class);
        assertEquals("FROMMAP", testConfig.dstr());
    }

    @Test public void firstSkipEmpty() {
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/notExists.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("A", testConfig.dstr());
    }

    @Test public void firstNotExists() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/notExists.properties").build().create(TestConfig.class));
    }

    @Test public void setLoadStrategyNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setLoadStrategy(null));
    }

    @Test public void firstIgnoreCaseHolder() {
        Map<String, String> manual = new HashMap<>();
        manual.put("test.String", "STR");
        ConfigHolder holder = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST_KEYCASEINSENSITIVE).setSource(manual).build()
                .configHolder();
        assertEquals("STR", holder.getString("test.strinG"));
    }

    @Test public void firstIgnoreCase() {
        Map<String, String> manual = new HashMap<>();
        manual.put("test.String", "STR");
        manual.put("TEST.LIST", "qq,ss");
        manual.put("tEst.SET", "a,v,x");
        manual.put("tEst.sort", "a,v,v");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST_KEYCASEINSENSITIVE).setSource(manual).build()
                .create(TestConfig.class);
        assertEquals("STR", testConfig.str());
    }

    @Test public void mergeIgnoreCase() {
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.MERGE_KEYCASEINSENSITIVE)
                .addSource("classpath:config/testconfigIgnoreKeyCase.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("IGNORECASE", testConfig.str());
    }

    @Test public void firstRelaxed() {
        Map<String, String> manual = new HashMap<>();
        manual.put("test-String", "STRr");
        manual.put("TEST.LIST", "qq,ss");
        manual.put("tEst_SET", "a,v,x");
        manual.put("tEst.sort", "a,v,v");
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST_KEYRELAXED).setSource(manual).build()
                .create(TestConfig.class);
        assertEquals("STRr", testConfig.str());
    }

    @Test public void mergeRelaxed() {
        TestConfig testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.MERGE_KEYRELAXED)
                .addSource("classpath:config/testconfigRelaxedKey.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RELAXED", testConfig.str());
    }

    @Test public void unknown() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigFactory.builder().setLoadStrategy(LoadStrategy.UNKNOWN).build().create(TestConfig.class));
    }
}
