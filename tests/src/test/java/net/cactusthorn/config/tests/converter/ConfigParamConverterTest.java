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

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.ConfigFactory;

public class ConfigParamConverterTest {

    @Test public void convert() {
        Map<String, String> properties = new HashMap<>();
        properties.put("localDate", "12.11.2011");
        ConfigParamConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(LocalDate.of(2011, 11, 12), config.localDate());
    }

    @Test public void convertDT() {
        Map<String, String> properties = new HashMap<>();
        properties.put("localDate", "12.11.2011");
        properties.put("localDateTime", "10.03.2016 08:11:33");
        ConfigParamConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(LocalDateTime.of(2016, 3, 10, 8, 11, 33), config.localDateTime().get());
    }

    @Test public void convertZDT() {
        Map<String, String> properties = new HashMap<>();
        properties.put("localDate", "12.11.2011");
        properties.put("zonedDateTime", "10.03.2016 08:11:33Z");
        ConfigParamConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertTrue(config.zonedDateTime().isPresent());
    }

    @Test public void localDateDefault() {
        Map<String, String> properties = new HashMap<>();
        properties.put("localDate", "12.11.2011");
        properties.put("localDateDefault", "2011-11-12");
        ConfigParamConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(LocalDate.of(2011, 11, 12), config.localDateDefault().get());
    }

    @Test public void offsetDateTime() {
        Map<String, String> properties = new HashMap<>();
        properties.put("localDate", "12.11.2011");
        properties.put("offsetDateTime", "12.11.2011T01:30:00+02:00");
        ConfigParamConverter config = ConfigFactory.builder().setSource(properties).build().create(ConfigParamConverter.class);
        assertEquals(OffsetDateTime.of(2011, 11, 12, 1, 30, 0, 0, ZoneOffset.of("+02:00")), config.offsetDateTime().get());
    }
}
