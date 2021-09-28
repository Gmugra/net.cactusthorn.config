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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.Loaders;

public class ConfigInitializerTest {

    private static final InitIt INIT_IT = new InitIt(null);

    public static final class InitIt extends ConfigInitializer {

        protected InitIt(Loaders loaders) {
            super(loaders);
        }

        @Override public Map<String, Object> initialize() {
            return null;
        }

        public String key(String key) {
            return expandKey(key);
        }
    }

    @Test public void simple() {
        assertEquals("value", INIT_IT.key("value"));
    }

    @Test public void propStart() {
        assertEquals("value", INIT_IT.key("{prop}.value"));
    }

    @Test public void propEnd() {
        assertEquals("value", INIT_IT.key("value.{prop}"));
    }

    @Test public void propMid() {
        assertEquals("xxx.yyy.value", INIT_IT.key("xxx.{a}.yyy.{bbb}.value"));
    }

    @Test public void propExpand() {
        System.setProperty("xx", "11");
        System.setProperty("yy", "22");
        assertEquals("xxx.11.yyy.22.value", INIT_IT.key("xxx.{xx}.yyy.{yy}.value"));
    }
}
