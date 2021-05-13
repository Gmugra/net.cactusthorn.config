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
package net.cactusthorn.config.tests.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class ConfigConverterTest {

    @Test public void url() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(new URL("https://www.google.com"), config.url());
    }

    @Test public void optionalUrl() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        properties.put("ourl", "https://www.bing.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(new URL("https://www.bing.com"), config.ourl().get());
    }

    @Test public void list() throws MalformedURLException {
        Map<String, String> properties = new HashMap<>();
        properties.put("url", "https://www.google.com");
        properties.put("list", "https://www.bing.com,https://github.com");
        ConfigConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigConverter.class);
        assertEquals(2, config.list().get().size());
    }

    @Test public void defaultUrl() throws MalformedURLException {
        ConfigConverter config = ConfigFactory.builder().build().create(ConfigConverter.class);
        assertEquals(new URL("https://github.com"), config.url());
    }
}
