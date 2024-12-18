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
package net.cactusthorn.config.tests.myconfig;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.factory.ConfigFactory;

class MyConfigTest {

    @Test void properties() {
        var myConfig = ConfigFactory.builder().addSource("classpath:myconfig.properties").build().create(MyConfig.class);
        assertEquals(URI.create("http://java.sun.com/j2se/1.3/"), myConfig.uri());
    }

    @Test void propertiesXML() {
        var myConfig = ConfigFactory.builder().addSource("classpath:myconfig.xml").build().create(MyConfig.class);
        assertEquals(LocalDate.of(2005, 11, 12), myConfig.date());
    }

    @Test void ownerXML() {
        var myConfig = ConfigFactory.builder().addSource("classpath:myconfig-owner.xml").build().create(MyConfig.class);
        assertTrue(myConfig.units().contains(TimeUnit.HOURS));
    }

    @Test void toml() {
        var myConfig = ConfigFactory.builder().addSource("classpath:myconfig.toml").build().create(MyConfig.class);
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"), myConfig.ids().get(1));
        assertEquals(LocalDate.of(2005, 11, 12), myConfig.date());
    }

}
