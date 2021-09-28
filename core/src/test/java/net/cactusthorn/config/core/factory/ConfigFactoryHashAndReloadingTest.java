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
package net.cactusthorn.config.core.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.cactusthorn.config.core.DisabledAutoReload;
import net.cactusthorn.config.core.TestConfig;
import net.cactusthorn.config.core.loader.ConfigHolder;

public class ConfigFactoryHashAndReloadingTest {

    @BeforeAll static void setUpLogger() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        // switch off default Handlers to do not get anything in console
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    @Test public void updateFile(@TempDir Path path) throws IOException, InterruptedException {
        URI uri = prepareTempFile(path, "testF.properties", "test.properties");
        ConfigFactory factory = ConfigFactory.builder().addSource(uri).build();
        ConfigHolder holder = factory.configHolder();
        assertEquals("bbb", holder.getString("aaa"));
        Thread.sleep(1000);
        prepareTempFile(path, "testF.properties", "test2.properties");
        holder = factory.configHolder();
        assertEquals("zzz", holder.getString("aaa"));
    }

    @Test public void updateFileAuto(@TempDir Path path) throws IOException, InterruptedException {
        URI uri = prepareTempFile(path, "testFA.properties", "test.properties");
        ConfigFactory factory = ConfigFactory.builder().addSource(uri).autoReload(2).build();
        TestConfig testConfig = factory.create(TestConfig.class);
        assertEquals("bbb", testConfig.aaa());
        Thread.sleep(1000);
        prepareTempFile(path, "testFA.properties", "test2.properties");
        Thread.sleep(3000);
        assertEquals("zzz", testConfig.aaa());
    }

    @Test public void disableUpdateFileAuto(@TempDir Path path) throws IOException, InterruptedException {
        URI uri = prepareTempFile(path, "testDFA.properties", "test.properties");
        ConfigFactory factory = ConfigFactory.builder().addSource(uri).autoReload(2).build();
        DisabledAutoReload config = factory.create(DisabledAutoReload.class);
        assertEquals("bbb", config.aaa());
        Thread.sleep(1000);
        prepareTempFile(path, "testDFA.properties", "test2.properties");
        Thread.sleep(3000);
        assertEquals("bbb", config.aaa());
    }

    private URI prepareTempFile(Path tempDir, String fileName, String resourceName) throws IOException {
        Path file = tempDir.resolve(fileName);
        try (InputStream stream = ConfigFactoryHashAndReloadingTest.class.getClassLoader().getResourceAsStream(resourceName)) {
            Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
        }
        return file.toUri();
    }
}
