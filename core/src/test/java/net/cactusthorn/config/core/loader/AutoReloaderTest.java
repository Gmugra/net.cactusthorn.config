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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.Reloadable;

class AutoReloaderTest {

    private static int counter = 0;
    private static int counter2 = 0;

    static class TestIt implements Reloadable {
        @Override public void reload() {
            counter++;
        }

        @Override public void addReloadListener(ReloadListener listener) {
            // do nothing
        }
    }

    static class DisableIt implements Reloadable {
        @Override public void reload() {
            counter2++;
        }

        @Override public boolean autoReloadable() {
            return false;
        }

        @Override public void addReloadListener(ReloadListener listener) {
            // do nothing
        }
    }

    @Test void simple() throws InterruptedException {
        counter = 0;
        var reloader = new AutoReloader(1);
        Thread.sleep(3000);
        assertEquals(0, counter);

        reloader.register(new TestIt());
        Thread.sleep(5000);
        assertTrue(counter >= 2);

        reloader.register(new TestIt());
        Thread.sleep(5000);
        assertTrue(counter >= 7);
    }

    @Test void disableIt() throws InterruptedException {
        counter2 = 0;
        var reloader = new AutoReloader(1);
        Thread.sleep(3000);
        assertEquals(0, counter2);
        reloader.register(new DisableIt());
        Thread.sleep(5000);
        assertEquals(0, counter2);
    }
}
