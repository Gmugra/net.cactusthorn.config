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
package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class LocalDateTimeConverterTest {

    static final LocalDateTime DATE = LocalDateTime.of(2016, 3, 10, 8, 11, 33);
    static final Converter<LocalDateTime> CONVERTER = new LocalDateTimeConverter();

    @Test public void simple() {
        LocalDateTime result = CONVERTER.convert("2016-03-10T08:11:33");
        assertEquals(DATE, result);
    }

    @Test public void simpleNullParameters() {
        LocalDateTime result = CONVERTER.convert("2016-03-10T08:11:33", null);
        assertEquals(DATE, result);
    }

    @Test public void simpleEmptyParameters() {
        LocalDateTime result = CONVERTER.convert("2016-03-10T08:11:33", new String[0]);
        assertEquals(DATE, result);
    }

    @Test public void complex() {
        LocalDateTime result = CONVERTER.convert("10.03.2016 08:11:33", new String[] { "yyyy-MM-dd'T'HH:mm:ss", "dd.MM.yyyy' 'HH:mm:ss" });
        assertEquals(DATE, result);
    }

    @Test public void singleParam() {
        LocalDateTime result = CONVERTER.convert("2016-03-10T08:11:33", new String[] { "yyyy-MM-dd'T'HH:mm:ss" });
        assertEquals(DATE, result);
    }
}
