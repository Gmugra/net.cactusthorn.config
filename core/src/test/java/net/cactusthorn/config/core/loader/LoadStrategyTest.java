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

import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.TestConfig;
import net.cactusthorn.config.core.factory.ConfigFactory;

class LoadStrategyTest {

    @Test void merge() {
        var testConfig = ConfigFactory.builder()
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").build()
                .create(TestConfig.class);
        assertEquals("SSSSSSSS", testConfig.dstr());
    }

    @Test void first() {
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties").build()
                .create(TestConfig.class);
        assertEquals("A", testConfig.dstr());
    }

    @Test void withMapMerge() {
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.MERGE)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties")
                .setSource(Map.of("test.dstr", "FROMMAP"))
                .build().create(TestConfig.class);
        assertEquals("FROMMAP", testConfig.dstr());
    }

    @Test void withMapFirst() {
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/testconfig.properties", "classpath:config/testconfig2.properties")
                .setSource(Map.of("test.dstr", "FROMMAP"))
                .build().create(TestConfig.class);
        assertEquals("FROMMAP", testConfig.dstr());
    }

    @Test void firstSkipEmpty() {
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/notExists.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("A", testConfig.dstr());
    }

    @Test void firstNotExists() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST)
                .addSource("classpath:config/notExists.properties").build().create(TestConfig.class));
    }

    @Test void setLoadStrategyNull() {
        assertThrows(IllegalArgumentException.class, () -> ConfigFactory.builder().setLoadStrategy(null));
    }

    @Test void firstIgnoreCaseHolder() {
        var holder = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST_KEYCASEINSENSITIVE)
            .setSource(Map.of("test.String", "STR")).build()
                .configHolder();
        assertEquals("STR", holder.getString("test.strinG"));
    }

    @Test void firstIgnoreCase() {
        var manual = Map.of("test.String", "STR", "TEST.LIST", "qq,ss", "tEst.SET", "a,v,x", "tEst.sort", "a,v,v");
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST_KEYCASEINSENSITIVE).setSource(manual).build()
                .create(TestConfig.class);
        assertEquals("STR", testConfig.str());
    }

    @Test void mergeIgnoreCase() {
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.MERGE_KEYCASEINSENSITIVE)
                .addSource("classpath:config/testconfigIgnoreKeyCase.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("IGNORECASE", testConfig.str());
    }

    @Test void firstRelaxed() {
        var manual = Map.of("test-String", "STRr", "TEST.LIST", "qq,ss", "tEst_SET", "a,v,x", "tEst.sort", "a,v,v");
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.FIRST_KEYRELAXED).setSource(manual).build()
                .create(TestConfig.class);
        assertEquals("STRr", testConfig.str());
    }

    @Test void mergeRelaxed() {
        var testConfig = ConfigFactory.builder().setLoadStrategy(LoadStrategy.MERGE_KEYRELAXED)
                .addSource("classpath:config/testconfigRelaxedKey.properties", "classpath:config/testconfig.properties").build()
                .create(TestConfig.class);
        assertEquals("RELAXED", testConfig.str());
    }

    @Test void unknown() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigFactory.builder().setLoadStrategy(LoadStrategy.UNKNOWN).build().create(TestConfig.class));
    }
}
