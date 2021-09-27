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
package net.cactusthorn.config.extras.zookeeper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.loader.Loader;

public class ZooKeeperLoaderAcceptTest {

    private static final Loader LOADER = new ZooKeeperLoader();

    @Test public void acceptSimple() {
        assertTrue(LOADER.accept(URI.create("zookeeper://localhost:7777,localhost:8888/basePath")));
    }

    @Test public void acceptFull() {
        String uri = "zookeeper://localhost:7777,localhost:8888/basePath?sessionTimeoutMs=10000&connectionTimeoutMs=2000";
        assertTrue(LOADER.accept(URI.create(uri)));
    }

    @Test public void notAcceptOpaque() {
        assertFalse(LOADER.accept(URI.create("zookeeper:localhost")));
    }

    @Test public void notAcceptNoAuthority() {
        assertFalse(LOADER.accept(URI.create("zookeeper:///basePath")));
    }

    @Test public void notAcceptScheme() {
        assertFalse(LOADER.accept(URI.create("http://localhost:7777,localhost:8888/basePath")));
    }

    @Test public void notAcceptNoPath() {
        assertFalse(LOADER.accept(URI.create("zookeeper://localhost:7777,localhost:8888")));
    }
}
